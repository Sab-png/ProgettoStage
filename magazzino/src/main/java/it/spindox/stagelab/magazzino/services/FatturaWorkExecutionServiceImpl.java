package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.FatturaWorkExecution.*;
import it.spindox.stagelab.magazzino.entities.*;
import it.spindox.stagelab.magazzino.exceptions.ResourceNotFoundException;
import it.spindox.stagelab.magazzino.exceptions.jobsexceptions.InvalidFatturaException;
import it.spindox.stagelab.magazzino.mappers.FatturaWorkExecutionMapper;
import it.spindox.stagelab.magazzino.repositories.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import static it.spindox.stagelab.magazzino.entities.SXFatturaStatus.determine;



@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FatturaWorkExecutionServiceImpl implements FatturaWorkExecutionService {

    private final FatturaWorkExecutionRepository fatturaWorkExecutionRepository;
    private final FatturaRepository fatturaRepository;
    private final FatturaWorkExecutionMapper fatturaWorkExecutionMapper;

    @PersistenceContext
    private final EntityManager entityManager;


    // ADD PAYMENT + CHECK STATUS

    private BigDecimal addPayment(Fattura fattura, BigDecimal pagatoDaAggiungere) {

        if (pagatoDaAggiungere == null || pagatoDaAggiungere.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Il pagamento deve essere > 0");

        if (fattura.getImporto() == null)
            throw new InvalidFatturaException("Importo non definito per la fattura.");

        BigDecimal pagatoCorrente = fattura.getPagato() == null
                ? BigDecimal.ZERO
                : fattura.getPagato();

        if (fattura.getStatus() == SXFatturaStatus.PAGATA)
            throw new InvalidFatturaException("La fattura risulta già pagata.");

        BigDecimal nuovoTotale = pagatoCorrente.add(pagatoDaAggiungere);

        if (nuovoTotale.compareTo(fattura.getImporto()) > 0)
            throw new InvalidFatturaException("Pagamento superiore all'importo totale.");

        return nuovoTotale;
    }

    // PAYMENT CHECK SINGOLA FATTURA

    @Override
    public FatturaWorkExecutionPaymentResponse paymentCheckFattura(Long workExecutionId, BigDecimal pagatoDaAggiungere) {

        FatturaWorkExecution exec = fatturaWorkExecutionRepository.findById(workExecutionId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkExecution non trovata"));

        Fattura fattura = fatturaRepository.findById(exec.getFatturaId())
                .orElseThrow(() -> new ResourceNotFoundException("Fattura non trovata"));

        // RUNNING

        exec.setStatus(SXFatturaJobexecution.RUNNING);
        fatturaWorkExecutionRepository.save(exec);

        BigDecimal nuovoTotale = addPayment(fattura, pagatoDaAggiungere);

        fattura.setPagato(nuovoTotale);
        fattura.setStatus(determine(fattura.getImporto(), nuovoTotale, fattura.getDataScadenza()));
        fatturaRepository.save(fattura);

        // SUCCESS

        exec.setStatus(SXFatturaJobexecution.SUCCESS);
        exec.setEndTime(OffsetDateTime.now(ZoneOffset.UTC));
        fatturaWorkExecutionRepository.save(exec);

        return fatturaWorkExecutionMapper.toPaymentResponse(fattura, exec);
    }

    // PAYMENT CHECK TUTTE LE FATTURE

    @Override
    public List<FatturaWorkExecutionPaymentResponse> paymentCheckAllFatture() {

        entityManager.clear();

        List<FatturaWorkExecution> executions = fatturaWorkExecutionRepository.findAll();
        List<Fattura> fattureDaAggiornare = new ArrayList<>();
        List<FatturaWorkExecution> execDaAggiornare = new ArrayList<>();
        List<FatturaWorkExecutionPaymentResponse> responses = new ArrayList<>();

        for (FatturaWorkExecution exec : executions) {

            Fattura f = fatturaRepository.findById(exec.getFatturaId()).orElse(null);

            if (f == null || f.getImporto() == null) {
                log.error("[AUTO CHECK] WorkExec ID={} fattura assente o importo nullo", exec.getId());
                exec.setStatus(SXFatturaJobexecution.ERROR);
                exec.setEndTime(OffsetDateTime.now(ZoneOffset.UTC));
                execDaAggiornare.add(exec);
                continue;
            }

            // Se era PENDING, passa a RUNNING

            if (exec.getStatus() == null || exec.getStatus() == SXFatturaJobexecution.PENDING) {
                exec.setStatus(SXFatturaJobexecution.RUNNING);
            }

            BigDecimal pagato = f.getPagato() == null ? BigDecimal.ZERO : f.getPagato();
            SXFatturaStatus nuovo = determine(f.getImporto(), pagato, f.getDataScadenza());

            boolean cambiata = f.getStatus() != nuovo;
            if (cambiata) {
                f.setStatus(nuovo);
                fattureDaAggiornare.add(f);
            }

            exec.setStatus(SXFatturaJobexecution.SUCCESS);
            exec.setEndTime(OffsetDateTime.now(ZoneOffset.UTC));
            execDaAggiornare.add(exec);

            responses.add(fatturaWorkExecutionMapper.toPaymentResponse(f, exec));
        }

        if (!fattureDaAggiornare.isEmpty()) {
            fatturaRepository.saveAllAndFlush(fattureDaAggiornare);
            log.info("SALVATE {} fatture modificate", fattureDaAggiornare.size());
        }

        if (!execDaAggiornare.isEmpty()) {
            fatturaWorkExecutionRepository.saveAllAndFlush(execDaAggiornare);
            log.info("SALVATE {} work execution aggiornate", execDaAggiornare.size());
        }

        return responses;
    }


    // METODO PER FIXARE I CAMPI NULL

    @Override
    public int fixNullFields() {

        entityManager.clear();

        List<FatturaWorkExecution> executions = fatturaWorkExecutionRepository.findAll();
        List<Fattura> fattureDaAggiornare = new ArrayList<>();

        for (FatturaWorkExecution exec : executions) {

            Fattura f = fatturaRepository.findById(exec.getFatturaId()).orElse(null);
            if (f == null) continue;

            boolean modificata = false;

            if (f.getPagato() == null) {
                f.setPagato(BigDecimal.ZERO);
                modificata = true;
            }

            if (f.getStatus() == null) {
                f.setStatus(SXFatturaStatus.EMESSA);
                modificata = true;
            }

            if (modificata) fattureDaAggiornare.add(f);
        }

        if (!fattureDaAggiornare.isEmpty()) {
            fatturaRepository.saveAllAndFlush(fattureDaAggiornare);
            log.info("FIX-NULL: ripulite {} fatture", fattureDaAggiornare.size());
        }

        return fattureDaAggiornare.size();
    }


    // SEARCH CON FILTRI E PAGINAZIONE

    @Override
    @Transactional(readOnly = true)
    public Page<FatturaWorkExecutionPaymentResponse> search(FatturaWorkExecutionSearch req) {

        Pageable pageable = PageRequest.of(
                Math.max(req.getPage(), 0),
                Math.max(req.getSize(), 1),
                Sort.by("startTime").descending()
        );

        Page<FatturaWorkExecution> result =
                fatturaWorkExecutionRepository.searchWorkExecutions(
                        req.getStatus(),
                        req.getStartFrom() != null ? req.getStartFrom().atOffset(ZoneOffset.UTC) : null,
                        req.getStartTo() != null ? req.getStartTo().atOffset(ZoneOffset.UTC) : null,
                        req.getFatturaId(),
                        req.getHasError(),
                        pageable
                );

        return result.map(exec -> {
            Fattura fattura = fatturaRepository.findById(exec.getFatturaId()).orElse(null);
            return fatturaWorkExecutionMapper.toPaymentResponse(fattura, exec);
        });
    }
}