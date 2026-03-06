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


      // Ritorna il totale già pagato. Se null da 0.

    private BigDecimal getPagatoCorrente(Fattura fattura) {
        return fattura.getPagato() == null
                ? BigDecimal.ZERO
                : fattura.getPagato();
    }

    private void validatePagamento(BigDecimal pagatoDaAggiungere) {
        if (pagatoDaAggiungere == null || pagatoDaAggiungere.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Il pagamento deve essere > 0");
        }
    }

    //   ADD PAYMENT
     //  Calcola il nuovo totale pagato, dopo aver validato:

    private BigDecimal addPayment(Fattura fattura, BigDecimal pagatoDaAggiungere) {

        // 1. Validazione del pagamento da aggiungere

        validatePagamento(pagatoDaAggiungere);

        // 2. Importo deve esistere

        if (fattura.getImporto() == null) {
            throw new InvalidFatturaException("Importo non definito per la fattura.");
        }
        // 3. Pagato attuale

        BigDecimal pagatoCorrente = getPagatoCorrente(fattura);

        // 4. Non puoi pagare una fattura già saldata

        if (fattura.getStatus() == SXFatturaStatus.PAGATA) {
            throw new InvalidFatturaException("La fattura risulta già pagata.");
        }

        // 5. Totale dopo il pagamento

        BigDecimal nuovoTotale = pagatoCorrente.add(pagatoDaAggiungere);

        // 6. Max totale = importo fattura

        if (nuovoTotale.compareTo(fattura.getImporto()) > 0) {
            throw new InvalidFatturaException("Pagamento superiore all'importo totale.");
        }

        return nuovoTotale;
    }


    //   PAGAMENTO SINGOLA FATTURA

    @Override
    public FatturaWorkExecutionPaymentResponse paymentCheckFattura(
            Long workExecutionId, BigDecimal pagatoDaAggiungere) {

        // 1. Recupero dei dati

        FatturaWorkExecution exec = fatturaWorkExecutionRepository.findById(workExecutionId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkExecution non trovata"));

        Fattura fattura = fatturaRepository.findById(exec.getFatturaId())
                .orElseThrow(() -> new ResourceNotFoundException("Fattura non trovata"));

        // 2. Stato RUNNING

        exec.setStatus(SXFatturaJobexecution.RUNNING);
        exec.setStartTime(OffsetDateTime.now(ZoneOffset.UTC));
        fatturaWorkExecutionRepository.save(exec);

        // 3. Calcolo nuovo pagamento

        BigDecimal nuovoTotale = addPayment(fattura, pagatoDaAggiungere);

        // 4. Aggiornamento dati fattura

        fattura.setPagato(nuovoTotale);
        fattura.setStatus(determine(fattura.getImporto(), nuovoTotale, fattura.getDataScadenza()));
        fatturaRepository.save(fattura);

        // 5. Stato SUCCESS

        exec.setStatus(SXFatturaJobexecution.SUCCESS);
        exec.setEndTime(OffsetDateTime.now(ZoneOffset.UTC));
        fatturaWorkExecutionRepository.save(exec);

        return fatturaWorkExecutionMapper.toPaymentResponse(fattura, exec);
    }


    //   CHECK SU TUTTE LE FATTURE


    @Override
    public List<FatturaWorkExecutionPaymentResponse> paymentCheckAllFatture() {

        entityManager.clear();

        List<FatturaWorkExecution> executions = fatturaWorkExecutionRepository.findAll();
        List<Fattura> fattureToUpdate = new ArrayList<>();
        List<FatturaWorkExecution> execToUpdate = new ArrayList<>();
        List<FatturaWorkExecutionPaymentResponse> responses = new ArrayList<>();

        for (FatturaWorkExecution exec : executions) {

            Fattura f = fatturaRepository.findById(exec.getFatturaId()).orElse(null);

            //  Caso 1: Fattura mancante / incoerente

            if (f == null || f.getImporto() == null) {

                exec.setStatus(SXFatturaJobexecution.ERROR);
                exec.setEndTime(OffsetDateTime.now(ZoneOffset.UTC));

                execToUpdate.add(exec);
                continue;
            }

            // RUNNING se è PENDING o null

            if (exec.getStatus() == null || exec.getStatus() == SXFatturaJobexecution.PENDING) {
                exec.setStatus(SXFatturaJobexecution.RUNNING);
            }

            // Ricalcolo stato fattura

            BigDecimal pagato = getPagatoCorrente(f);
            SXFatturaStatus nuovoStatus = determine(f.getImporto(), pagato, f.getDataScadenza());

            if (f.getStatus() != nuovoStatus) {
                f.setStatus(nuovoStatus);
                fattureToUpdate.add(f);
            }

            // SUCCESS

            exec.setStatus(SXFatturaJobexecution.SUCCESS);
            exec.setEndTime(OffsetDateTime.now(ZoneOffset.UTC));
            execToUpdate.add(exec);

            responses.add(fatturaWorkExecutionMapper.toPaymentResponse(f, exec));
        }

        //  Dati modificati

        if (!fattureToUpdate.isEmpty()) {
            fatturaRepository.saveAllAndFlush(fattureToUpdate);
            log.info("SALVATE {} fatture modificate", fattureToUpdate.size());
        }

        if (!execToUpdate.isEmpty()) {
            fatturaWorkExecutionRepository.saveAllAndFlush(execToUpdate);
            log.info("SALVATE {} work execution aggiornate", execToUpdate.size());
        }

        return responses;
    }


    //   FIX CAMPI NULL


    @Override
    public int fixNullFields() {


        //svuota la cache

        entityManager.clear();

        // Recupera tutte le WorkExecution
        List<FatturaWorkExecution> executions = fatturaWorkExecutionRepository.findAll();
        List<Fattura> fattureDaAggiornare = new ArrayList<>();


        for (FatturaWorkExecution exec : executions) {

            // Recupera la fattura collegata

            Fattura f = fatturaRepository.findById(exec.getFatturaId()).orElse(null);

            // Se la fattura non esiste più la salta

            if (f == null) continue;

            boolean modificata = false;

            // Se pagato è null  lo inizializza a ZERO

            if (f.getPagato() == null) {
                f.setPagato(BigDecimal.ZERO);
                modificata = true;
            }

            // Se status è null
            // lo imposta a EMESSA
            if (f.getStatus() == null) {
                f.setStatus(SXFatturaStatus.EMESSA);
                modificata = true;
            }


            if (modificata) fattureDaAggiornare.add(f);
        }

        // Salva TUTTE le fatture modificate

        if (!fattureDaAggiornare.isEmpty()) {
            fatturaRepository.saveAllAndFlush(fattureDaAggiornare);
            log.info("FIX-NULL: ripulite {} fatture", fattureDaAggiornare.size());
        }

        // Ritorna quante fatture sono state modificate

        return fattureDaAggiornare.size();
    }

    @Override
    public Page<FatturaWorkExecutionPaymentResponse> search(FatturaWorkExecutionSearch req) {
        return null;
    }
}

