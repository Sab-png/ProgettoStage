package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.jobExecution.JobExecutionRequest;
import it.spindox.stagelab.magazzino.dto.jobExecution.JobExecutionResponse;
import it.spindox.stagelab.magazzino.entities.JobExecution;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import it.spindox.stagelab.magazzino.entities.StatusJobErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;


@Slf4j
@Component
public class JobExecutionMapperImpl implements JobExecutionMapper {

    // OFFSET :  LOCAL

    private LocalDateTime toLocal(OffsetDateTime odt) {
        return (odt != null) ? odt.toLocalDateTime() : null;
    }

    // LOCAL : OFFSET (UTC)
    private OffsetDateTime toUtc(LocalDateTime ldt) {
        return (ldt != null) ? ldt.atOffset(ZoneOffset.UTC) : null;
    }

    //  FACTORY: crea una nuova entity da nome job e stato

    @Override
    public JobExecution toEntity(String jobName, StatusJob status) {
        if (jobName == null && status == null) {
            log.warn("toEntity(jobName, status) chiamato con entrambi i parametri NULL");
        }

        JobExecution e = new JobExecution();
        e.setJobName(jobName);
        e.setStatus(status);
        e.setStartTime(OffsetDateTime.now(ZoneOffset.UTC)); // start in UTC
        e.setUpdateDate(OffsetDateTime.now(ZoneOffset.UTC)); // audit in UTC
        // e.setEndTime(null); // opzionale: lascialo null finché il job non termina
        return e;
    }

    //  ENTITY : RESPONSE DTO

    @Override
    public JobExecutionResponse toResponse(JobExecution entity) {
        if (entity == null) return null;

        return JobExecutionResponse.builder()
                .id(entity.getId())
                .status(entity.getStatus())
                .startTime(toLocal(entity.getStartTime()))
                .endTime(toLocal(entity.getEndTime()))
                .errorType(entity.getErrorType())
                .errorMessage(entity.getErrorMessage())
                .build();
    }

    // UPDATE : senza errorType

    @Override
    public void updateEntity(JobExecution target, StatusJob status, String errorMessage) {
        update(target, status, null, errorMessage);
    }

    // --- UPDATE : con errorType

    @Override
    public void updateEntity(JobExecution target, StatusJob status, StatusJobErrorType errorType, String errorMessage) {
        update(target, status, errorType, errorMessage);
    }


    private void update(JobExecution target, StatusJob status, StatusJobErrorType errorType, String errorMessage) {

        log.info(
                "Aggiornamento JobExecution (id={}): status={}, errorType={}, errorMessage={}",
                target != null ? target.getId() : null, status, errorType, errorMessage
        );

        if (target == null) {
            log.warn("updateEntity chiamato con target NULL");
            return;
        }

        target.setStatus(status);
        target.setErrorType(errorType);
        target.setErrorMessage(errorMessage);
        target.setUpdateDate(OffsetDateTime.now(ZoneOffset.UTC)); // coerente: sempre UTC

        log.debug("JobExecution aggiornato: {}", target);
    }

    // REQUEST DTO : ENTITY

    @Override
    public JobExecution toEntity(JobExecutionRequest req) {
        if (req == null) return null;

        JobExecution e = new JobExecution();
        e.setStartTime(toUtc(req.getFrom()));
        e.setEndTime(toUtc(req.getTo()));
        return e;
    }
}
