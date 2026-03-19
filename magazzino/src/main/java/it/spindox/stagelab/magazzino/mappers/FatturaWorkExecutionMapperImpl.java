package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.FatturaWorkExecution.DtoPaymentResponse;
import it.spindox.stagelab.magazzino.entities.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.time.*;



@Slf4j
@Component
public class FatturaWorkExecutionMapperImpl implements FatturaWorkExecutionMapper {

    // Converte un OffsetDateTime in LocalDateTime

    private LocalDateTime toLocal(OffsetDateTime odt) {
        return odt != null ? odt.toLocalDateTime() : null;
    }

    // Converte una LocalDate in un OffsetDateTime

    private OffsetDateTime toOffset(LocalDate ld) {
        return ld != null ? ld.atStartOfDay().atOffset(ZoneOffset.UTC) : null;
    }

    // Crea una nuova WorkExecution collegata a una fattura

    @Override
    public FatturaWorkExecution toEntity(String workName,
                                         SXFatturaJobexecution status,
                                         Fattura fattura) {

        FatturaWorkExecution exec = new FatturaWorkExecution();

        exec.setStartTime(OffsetDateTime.now(ZoneOffset.UTC));

        if (fattura != null) {
            exec.setFatturaId(fattura.getId());
        }

        exec.setStatus(status);

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
    public DtoPaymentResponse toPaymentResponse(Fattura fattura,
                                                FatturaWorkExecution exec) {

        if (fattura == null && exec == null) return null;

        return DtoPaymentResponse.builder()


                // ID DELLA WORK EXECUTION

                .workexecutionid(exec != null ? exec.getId() : null)


                // ID DELLA FATTURA

                .fatturaId(exec != null ? exec.getFatturaId() : null)


                // DATI FATTURA

                .status(fattura != null ? fattura.getStatus() : null)
                .importo(fattura != null ? fattura.getImporto() : null)
                .pagato(fattura != null ? fattura.getPagato() : null)
                .dataScadenza(fattura != null ?
                        toOffset(fattura.getDataScadenza()) : null)


                // DATI WORK EXECUTION

                .startTime(exec != null ? toLocal(exec.getStartTime()) : null)
                .endTime(exec != null ? toLocal(exec.getEndTime()) : null)
                .fatturaErrorType(exec != null ? exec.getErrorType() : null)

                // messaggio di errore
                .errorMessage(exec != null ? exec.getErrorMessage() : null)

                .build();
    }

    // Aggiorna solo stato e messaggio di errore

    @Override
    public void updateEntity(FatturaWorkExecution target,
                             SXFatturaJobexecution status,
                             String errorMessage) {
        update(target, status, null, null, errorMessage);
    }

    // Aggiorna stato + tipo errore job

    @Override
    public void updateEntity(FatturaWorkExecution target,
                             SXFatturaJobexecution status,
                             StatusJobErrorType errorType,
                             String errorMessage) {
        update(target, status, errorType, null, errorMessage);
    }

    // Aggiorna stato + tipo errore di fattura

    @Override
    public void updateEntity(FatturaWorkExecution target,
                             SXFatturaJobexecution status,
                             SXFatturaJobexecutionErrorType errorType,
                             String errorMessage) {
        update(target, status, null, errorType, errorMessage);
    }

    // Metodo che esegue effettivamente l’aggiornamento

    private void update(FatturaWorkExecution target,
                        SXFatturaJobexecution status,
                        StatusJobErrorType jobErrorType,
                        SXFatturaJobexecutionErrorType fatturaErrorType,
                        String errorMessage) {

        if (target == null) {
            log.warn("updateEntity chiamato con target null");
            return;
        }

        // Aggiorna lo stato se presente
        if (status != null) {
            target.setStatus(status);
        }

        // Se si presenta un errore del Job converto per nome all'errore di fattura
        if (jobErrorType != null) {
            try {
                SXFatturaJobexecutionErrorType converted =
                        SXFatturaJobexecutionErrorType.valueOf(jobErrorType.name());
                target.setErrorType(converted);
            } catch (IllegalArgumentException ex) {
                // Fallback prudente
                target.setErrorType(SXFatturaJobexecutionErrorType.UNKNOWN);
            }
        }

        // Se arriva errore della fattura viene segnato

        if (fatturaErrorType != null) {
            target.setErrorType(fatturaErrorType);
        }

        // Messaggio di errore

        target.setErrorMessage(errorMessage);

        // EndTime = momento in cui si chiude l’esecuzione

        target.setEndTime(OffsetDateTime.now(ZoneOffset.UTC));
    }
}