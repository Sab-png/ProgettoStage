package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.FatturaWorkExecution.FatturaWorkExecutionPaymentResponse;
import it.spindox.stagelab.magazzino.entities.Fattura;
import it.spindox.stagelab.magazzino.entities.FatturaWorkExecution;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import it.spindox.stagelab.magazzino.entities.StatusJobErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import it.spindox.stagelab.magazzino.entities.*;


@Slf4j
@Component

public class FatturaWorkExecutionMapperImpl implements FatturaWorkExecutionMapper {

    // Converte OffsetDateTime in LocalDateTime per la response

    private LocalDateTime toLocal(OffsetDateTime odt) {
        return odt != null ? odt.toLocalDateTime() : null;
    }

    // Crea una nuova execution di job

    @Override

    public FatturaWorkExecution toEntity(String workName, StatusJob status, Fattura fattura) {

        FatturaWorkExecution exec = new FatturaWorkExecution();

        // Se status è null, setto RUNNING di default

        exec.setStatus(status != null ? status : StatusJob.RUNNING);

        // Timestamp di inizio job

        exec.setStartTime(OffsetDateTime.now(ZoneOffset.UTC));

        // Collegamento alla fattura (solo ID)

        if (fattura != null)
            exec.setFatturaId(fattura.getId());

        return exec;
    }

    // Costruisce la response (dati fattura + dati job)

    @Override
    public FatturaWorkExecutionPaymentResponse toPaymentResponse(Fattura fattura, FatturaWorkExecution exec) {

        if (fattura == null && exec == null) return null;

        return FatturaWorkExecutionPaymentResponse.builder()
                //  DATI della FATTURA

                .id(fattura != null ? fattura.getId() : null)
                .status(fattura != null ? fattura.getStatus() : null)
                .importo(fattura != null ? fattura.getImporto() : null)
                .pagato(fattura != null ? fattura.getPagato() : null)
                .dataScadenza(fattura != null ? OffsetDateTime.from(fattura.getDataScadenza()) : null)

                // ---- DATI JOB EXECUTION ----
                .startTime(exec != null ? toLocal(exec.getStartTime()) : null)
                .endTime(exec != null ? toLocal(exec.getEndTime()) : null)
                .errorType(exec != null ? exec.getErrorType() : null)
                .errorMessage(exec != null ? exec.getErrorMessage() : null)

                .build();
    }

    // Aggiorna solo lo stato e il messaggio di errore

    @Override
    public void updateEntity(FatturaWorkExecution target, StatusJob status, String errorMessage) {
        update(target, status, null, errorMessage);
    }

    @Override
    public void updateEntity(FatturaWorkExecution target,
                             StatusJob status,
                             StatusJobErrorType errorType,
                             String errorMessage) {
        update(target, status, errorType, errorMessage);
    }

    //  update dello stato job

    private void update(FatturaWorkExecution target,
                        StatusJob status,
                        StatusJobErrorType errorType,
                        String errorMessage) {

        if (target == null) {
            log.warn("updateEntity chiamato con target null");
            return;
        }

        target.setStatus(status);
        target.setErrorType(errorType);
        target.setErrorMessage(errorMessage);
    }
}