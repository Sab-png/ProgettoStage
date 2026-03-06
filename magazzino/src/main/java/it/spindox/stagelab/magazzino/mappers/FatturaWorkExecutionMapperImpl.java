package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.FatturaWorkExecution.FatturaWorkExecutionPaymentResponse;
import it.spindox.stagelab.magazzino.entities.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.*;







@Slf4j
@Component
public class FatturaWorkExecutionMapperImpl implements FatturaWorkExecutionMapper {


     // Converte un OffsetDateTime in LocalDateTime.

    private LocalDateTime toLocal(OffsetDateTime odt) {
        return odt != null ? odt.toLocalDateTime() : null;
    }


     // Converte una LocalDate in un OffsetDateTime

    private OffsetDateTime toOffset(LocalDate ld) {
        return ld != null ? ld.atStartOfDay().atOffset(ZoneOffset.UTC) : null;
    }


      // Crea una nuova WorkExecution collegata a una fattura.

    @Override
    public FatturaWorkExecution toEntity(String workName,
                                         SXFatturaJobexecution status,
                                         Fattura fattura) {

        FatturaWorkExecution exec = new FatturaWorkExecution();

        // Se non passi uno stato DA COME RISULTATO IL PENDING

        exec.setStatus(status != null ? status : SXFatturaJobexecution.PENDING);

        // Orario di inizio esecuzione

        exec.setStartTime(OffsetDateTime.now(ZoneOffset.UTC));

        // Collegamento all’id della fattura

        if (fattura != null) {
            exec.setFatturaId(fattura.getId());
        }

        return exec;
    }

    @Override
    public FatturaWorkExecution toEntity(String workName,
                                         StatusJob status,
                                         Fattura fattura) {
        return null;
    }


     // Costruisce la response (DTO) contenente i dati della fattura e della work execution

    @Override
    public FatturaWorkExecutionPaymentResponse toPaymentResponse(Fattura fattura,
                                                                 FatturaWorkExecution exec) {

        // Se entrambi null → nessun dato da mostrare
        if (fattura == null && exec == null) return null;

        return FatturaWorkExecutionPaymentResponse.builder()

                // ---------- DATI FATTURA ----------
                .id(fattura != null ? fattura.getId() : null)
                .status(fattura != null ? fattura.getStatus() : null)
                .importo(fattura != null ? fattura.getImporto() : null)
                .pagato(fattura != null ? fattura.getPagato() : null)

                // Converte LocalDate → OffsetDateTime
                .dataScadenza(fattura != null
                        ? toOffset(fattura.getDataScadenza())
                        : null)

                // ---------- DATI WORK EXECUTION ----------
                .startTime(exec != null ? toLocal(exec.getStartTime()) : null)
                .endTime(exec != null ? toLocal(exec.getEndTime()) : null)
                .errorType(exec != null ? exec.getErrorType() : null)
                .errorMessage(exec != null ? exec.getErrorMessage() : null)

                .build();
    }

    /**
     * Aggiorna solo stato e messaggio di errore (senza errorType).
     */
    @Override
    public void updateEntity(FatturaWorkExecution target,
                             SXFatturaJobexecution status,
                             String errorMessage) {
        update(target, status, null, errorMessage);
    }

    /**
     * Aggiorna stato, errorType e message di una WorkExecution.
     */
    @Override
    public void updateEntity(FatturaWorkExecution target,
                             SXFatturaJobexecution status,
                             StatusJobErrorType errorType,
                             String errorMessage) {
        update(target, status, errorType, errorMessage);
    }

    /**
     * Metodo interno che esegue effettivamente l’aggiornamento:
     * - cambia stato
     * - imposta errorType e errorMessage
     * - setta endTime = ora attuale (chiude l’esecuzione)
     */
    private void update(FatturaWorkExecution target,
                        SXFatturaJobexecution status,
                        StatusJobErrorType errorType,
                        String errorMessage) {

        if (target == null) {
            log.warn("updateEntity chiamato con target null");
            return;
        }

        // Aggiorna lo stato se presente
        if (status != null)
            target.setStatus(status);

        // Aggiorna info errore
        target.setErrorType(errorType);
        target.setErrorMessage(errorMessage);

        // EndTime = momento in cui si chiude l’esecuzione
        target.setEndTime(OffsetDateTime.now(ZoneOffset.UTC));
    }
}