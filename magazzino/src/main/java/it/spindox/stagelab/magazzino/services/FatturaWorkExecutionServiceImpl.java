package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.FatturaWorkExecution.DtoPaymentResponse;
import it.spindox.stagelab.magazzino.dto.FatturaWorkExecution.DtoSearch;
import it.spindox.stagelab.magazzino.entities.*;
import it.spindox.stagelab.magazzino.exceptions.ResourceNotFoundException;
import it.spindox.stagelab.magazzino.exceptions.jobsexceptions.FatturaWorkExecutionException;
import it.spindox.stagelab.magazzino.exceptions.jobsexceptions.InvalidFatturaException;
import it.spindox.stagelab.magazzino.mappers.FatturaWorkExecutionMapper;
import it.spindox.stagelab.magazzino.repositories.FatturaRepository;
import it.spindox.stagelab.magazzino.repositories.FatturaWorkExecutionRepository;
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
    private final EntityManager entityManager; // per la cache (kesh)


    //  Ritorna il totale già pagato ,  Se null da 0.

    private BigDecimal getPagatoCorrente(Fattura fattura) {
        return fattura.getPagato() == null ? BigDecimal.ZERO : fattura.getPagato();
    }

    private void validatePagamento(BigDecimal pagatoDaAggiungere) {
        if (pagatoDaAggiungere == null || pagatoDaAggiungere.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Il pagamento deve essere > 0");
        }
    }
// AGGIUNGE UN EVENTUALE PAGAMENTO

    private BigDecimal addPayment(Fattura fattura, BigDecimal pagatoDaAggiungere) {

        validatePagamento(pagatoDaAggiungere);

        if (fattura.getImporto() == null)
            throw new InvalidFatturaException("Importo non definito per la fattura.");

        BigDecimal pagatoCorrente = getPagatoCorrente(fattura);

        if (fattura.getStatus() == SXFatturaStatus.PAGATA)
            throw new InvalidFatturaException("La fattura risulta già pagata.");

        BigDecimal nuovoTotale = pagatoCorrente.add(pagatoDaAggiungere);

        if (nuovoTotale.compareTo(fattura.getImporto()) > 0)
            throw new InvalidFatturaException("Pagamento superiore all'importo totale.");

        return nuovoTotale;
    }


    //   PAGAMENTO SINGOLA FATTURA

    @Override
    public DtoPaymentResponse paymentCheckFattura(Long workExecutionId, BigDecimal pagatoDaAggiungere) {

        FatturaWorkExecution exec = fatturaWorkExecutionRepository.findById(workExecutionId)
                .orElseThrow(() -> new ResourceNotFoundException("WorkExecution non trovata"));

        Fattura fattura = fatturaRepository.findById(exec.getFatturaId())
                .orElseThrow(() -> new ResourceNotFoundException("Fattura non trovata"));

        exec.setStatus(SXFatturaJobexecution.RUNNING);
        exec.setStartTime(OffsetDateTime.now(ZoneOffset.UTC));
        fatturaWorkExecutionRepository.save(exec);

        try {
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

        } catch (InvalidFatturaException e) {

            // Business error
            throw handleFailure(exec, fattura, SXFatturaJobexecutionErrorType.BUSINESS_ERROR, e.getMessage());

        } catch (Exception e) {

            // System error
            throw handleFailure(exec, fattura, SXFatturaJobexecutionErrorType.SYSTEM_ERROR, e.getMessage());
        }
    }


    // Traccia errore (status=ERROR, errorType, errorMessage, endTime)
    // Ritorna l'eccezione da rilanciare

    private FatturaWorkExecutionException handleFailure(FatturaWorkExecution exec,
                                                        Fattura fattura,
                                                        SXFatturaJobexecutionErrorType errorType,
                                                        String message) {

        fatturaWorkExecutionMapper.updateEntity(
                exec,
                SXFatturaJobexecution.ERROR,
                errorType,
                message
        );
        fatturaWorkExecutionRepository.save(exec);

        log.error("[PAYMENT-CHECK-FATTURA] workExecId={} fatturaId={} type={} msg={}",
                exec.getId(),
                (fattura != null ? fattura.getId() : null),
                errorType,
                message
        );

        return new FatturaWorkExecutionException(
                errorType,
                message,
                "workExecutionId=" + exec.getId(),
                (fattura != null ? fattura.getId() : null),
                exec.getId(),
                SXFatturaJobexecution.ERROR
        );
    }


    //   CHECK SU TUTTE LE FATTURE

    @Override
    @Transactional
    public List<DtoPaymentResponse> paymentCheckAllFatture() {

        entityManager.clear();

        //  PROCESSA LE FATTURE NON SOLO EMESSA
        List<Fattura> fatture = fatturaRepository.findAllByStatusIn(
                List.of(
                        SXFatturaStatus.EMESSA,
                        SXFatturaStatus.SCADUTA,
                        SXFatturaStatus.PAGATA
                )
        );

        List<Fattura> fattureToUpdate = new ArrayList<>();
        List<FatturaWorkExecution> execToSave = new ArrayList<>();

        List<DtoPaymentResponse> problems = new ArrayList<>();

        for (Fattura f : fatture) {

            try {
                // CASO ERRORE : IMPORTO NULL
                if (f.getImporto() == null) {
                    FatturaWorkExecution exec = newExecution(f, SXFatturaJobexecution.ERROR);
                    fatturaWorkExecutionMapper.updateEntity(
                            exec,
                            SXFatturaJobexecution.ERROR,
                            SXFatturaJobexecutionErrorType.SYSTEM_ERROR,
                            "Importo nullo"
                    );
                    execToSave.add(exec);
                    problems.add(fatturaWorkExecutionMapper.toPaymentResponse(f, exec));
                    continue;
                }

                // STATUS NORMALE
                BigDecimal pagato = getPagatoCorrente(f);
                SXFatturaStatus nuovoStatus = determine(
                        f.getImporto(),
                        pagato,
                        f.getDataScadenza()
                );

                // CASO DI BUSINESS WARNING : FATTURA SCADUTA
                if (nuovoStatus == SXFatturaStatus.SCADUTA) {

                    FatturaWorkExecution exec = newExecution(f, SXFatturaJobexecution.SUCCESS);
                    fatturaWorkExecutionMapper.updateEntity(
                            exec,
                            SXFatturaJobexecution.SUCCESS,
                            SXFatturaJobexecutionErrorType.BUSINESS_WARNING,
                            "Fattura non saldata entro la data di scadenza"
                    );

                    if (!f.getStatus().equals(nuovoStatus)) {
                        f.setStatus(nuovoStatus);
                        fattureToUpdate.add(f);
                    }

                    execToSave.add(exec);
                    problems.add(fatturaWorkExecutionMapper.toPaymentResponse(f, exec));
                    continue;
                }

                // SUCCESS senza errori
                if (!f.getStatus().equals(nuovoStatus)) {
                    f.setStatus(nuovoStatus);
                    fattureToUpdate.add(f);
                }

                FatturaWorkExecution exec = newExecution(f, SXFatturaJobexecution.SUCCESS);
                execToSave.add(exec);

            } catch (FatturaWorkExecutionException ex) {

                FatturaWorkExecution exec = newExecution(f, SXFatturaJobexecution.ERROR);

                fatturaWorkExecutionMapper.updateEntity(
                        exec,
                        ex.getExecutionStatus() != null ? ex.getExecutionStatus() : SXFatturaJobexecution.ERROR,
                        ex.getFatturaErrorType(),
                        ex.getMessage()
                );

                execToSave.add(exec);
                problems.add(fatturaWorkExecutionMapper.toPaymentResponse(f, exec));

                log.error("[BATCH-FATTURE] WorkExecution error fatturaId={} type={} msg={}",
                        f.getId(), ex.getFatturaErrorType(), ex.getMessage());

            } catch (Exception ex) {

                FatturaWorkExecution exec = newExecution(f, SXFatturaJobexecution.ERROR);

                fatturaWorkExecutionMapper.updateEntity(
                        exec,
                        SXFatturaJobexecution.ERROR,
                        SXFatturaJobexecutionErrorType.SYSTEM_ERROR,
                        ex.getMessage()
                );

                execToSave.add(exec);
                problems.add(fatturaWorkExecutionMapper.toPaymentResponse(f, exec));

                log.error("[BATCH-FATTURE] Unexpected error fatturaId={} msg={}",
                        f.getId(), ex.getMessage(), ex);
            }
        }

        // SALVATAGGI DB
        if (!fattureToUpdate.isEmpty())
            fatturaRepository.saveAllAndFlush(fattureToUpdate);

        if (!execToSave.isEmpty())
            fatturaWorkExecutionRepository.saveAllAndFlush(execToSave);

        return problems;
    }


    //   CREATE NEW EXECUTION

    private FatturaWorkExecution newExecution(Fattura f, SXFatturaJobexecution status) {

        FatturaWorkExecution exec = new FatturaWorkExecution();

        exec.setFatturaId(f.getId());
        exec.setStartTime(OffsetDateTime.now(ZoneOffset.UTC));
        exec.setEndTime(OffsetDateTime.now(ZoneOffset.UTC));
        exec.setStatus(status);

        return exec;
    }


    // FIX NULL FIELDS

    @Override
    @Transactional
    public int fixNullFields() {

        entityManager.clear();

        List<Fattura> fatture = fatturaRepository.findAll();
        List<Fattura> fattureDaAggiornare = new ArrayList<>();

        for (Fattura f : fatture) {
            boolean modificata = false;

            if (f.getPagato() == null) {
                f.setPagato(BigDecimal.ZERO);
                modificata = true;
            }

            if (f.getStatus() == null) {
                f.setStatus(EMESSA);
                modificata = true;
            }

            if (modificata)
                fattureDaAggiornare.add(f);
        }

        if (!fattureDaAggiornare.isEmpty()) {
            fatturaRepository.saveAllAndFlush(fattureDaAggiornare);
            log.info("FIX-NULL: ripulite {} fatture", fattureDaAggiornare.size());
        }

        return fattureDaAggiornare.size();
    }


    //   SEARCH

    @Override
    public Page<DtoPaymentResponse> search(DtoSearch req) {

        Set<String> sortableFields = Set.of(
                "id", "fatturaId", "startTime", "endTime",
                "errorType", "errorMessage", "status"
        );

        String sortBy = sortableFields.contains(req.getSortBy())
                ? req.getSortBy()
                : "startTime";

        Sort sort = Sort.by(req.getSortDir(), sortBy);
        Pageable pageable = PageRequest.of(req.getPage(), req.getSize(), sort);

        Page<FatturaWorkExecution> page = fatturaWorkExecutionRepository.findAll(pageable);

        return page.map(exec ->
                fatturaWorkExecutionMapper.toPaymentResponse(
                        fatturaRepository.findById(exec.getFatturaId()).orElse(null),
                        exec
                )
        );
    }
}
