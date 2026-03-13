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
import java.util.Set;

import static it.spindox.stagelab.magazzino.entities.SXFatturaJobexecution.SUCCESS;
import static it.spindox.stagelab.magazzino.entities.SXFatturaStatus.EMESSA;
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


    // Ritorna il totale già pagato ,  Se null da 0.

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
    public DtoPaymentResponse paymentCheckFattura(
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

        exec.setStatus(SUCCESS);
        exec.setEndTime(OffsetDateTime.now(ZoneOffset.UTC));
        fatturaWorkExecutionRepository.save(exec);

        return fatturaWorkExecutionMapper.toPaymentResponse(fattura, exec);
    }


    //   CHECK SU TUTTE LE FATTURE

    @Override
    @Transactional
    public List<DtoPaymentResponse> paymentCheckAllFatture() {

        entityManager.clear();

        List<Fattura> fatture = fatturaRepository.findAllByStatus(EMESSA);
        // FIND BY STATUS EMESSO
        List<Fattura> fattureToUpdate = new ArrayList<>();
        List<FatturaWorkExecution> execToSave = new ArrayList<>();

        //  lista della response con le eventuali problematiche

        List<DtoPaymentResponse> problems = new ArrayList<>();
// dto : a posto di fattura work execution payment response
        for (Fattura f : fatture) {

            if (f.getImporto() == null) {
                FatturaWorkExecution exec = newExecution(f, SXFatturaJobexecution.ERROR);
                execToSave.add(exec);

                //  SOLO ERROR

                problems.add(fatturaWorkExecutionMapper.toPaymentResponse(f, exec));
                continue;
            }

            BigDecimal pagato = getPagatoCorrente(f);

            SXFatturaStatus nuovoStatus = SXFatturaStatus.determine(
                    f.getImporto(),
                    pagato,
                    f.getDataScadenza()
            );

            boolean statoCambiato = !f.getStatus().equals (nuovoStatus);

            if (statoCambiato) {
                f.setStatus(nuovoStatus);
                fattureToUpdate.add(f);
            }


            FatturaWorkExecution exec = newExecution(f, SUCCESS);
            execToSave.add(exec);

        }

        if (!fattureToUpdate.isEmpty()) {
            fatturaRepository.saveAllAndFlush(fattureToUpdate);
        }

        if (!execToSave.isEmpty()) {
            fatturaWorkExecutionRepository.saveAllAndFlush(execToSave);
        }

        //  Restituisci i problemi

        return problems;
    }

//  crea una nuova WorkExecution

    private FatturaWorkExecution newExecution(Fattura f, SXFatturaJobexecution status) {

        FatturaWorkExecution exec = new FatturaWorkExecution();
        exec.setFatturaId(f.getId());
        exec.setStartTime(OffsetDateTime.now(ZoneOffset.UTC));
        exec.setEndTime(OffsetDateTime.now(ZoneOffset.UTC));
        exec.setStatus(status);

        return exec;
    }

    //   FIX CAMPI NULL


    @Override
    @Transactional
    public int fixNullFields() {

        // Svuota la cache (kesh)

        entityManager.clear();

        // Recupera tutte le fatture

        List<Fattura> fatture = fatturaRepository.findAll();

        List<Fattura> fattureDaAggiornare = new ArrayList<>();

        for (Fattura f : fatture) {
            if (f == null) continue;

            boolean modificata = false;

            // Inizializza pagato a ZERO se nullo

            if (f.getPagato() == null) {
                f.setPagato(BigDecimal.ZERO);
                modificata = true;
            }

            // Imposta stato a EMESSA se nullo

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

        return fattureDaAggiornare.size();
    }

// SEARCH FUNCTIONS

    @Override
    public Page<DtoPaymentResponse> search(DtoSearch req) {

        // dto caratteristiche + sort by ( ordinamento)

        Set<String> sortableFields = Set.of(
                "id", "fatturaId", "startTime", "endTime",
                "errorType", "errorMessage", "status"
        );

        String sortBy = sortableFields.contains(req.getSortBy())
                ? req.getSortBy()
                : "startTime";

        Sort sort = Sort.by(req.getSortDir(), sortBy);
        Pageable pageable = PageRequest.of(req.getPage(), req.getSize(), sort);
// page

        Page<FatturaWorkExecution> page =
                fatturaWorkExecutionRepository.findAll(pageable);

        return page.map(exec ->
                fatturaWorkExecutionMapper.toPaymentResponse(
                        fatturaRepository.findById(exec.getFatturaId()).orElse(null),
                        exec
                )
        );
    }
}