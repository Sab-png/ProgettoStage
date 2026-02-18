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

    //
    // OFFSET : LOCAL

    private LocalDateTime toLocal(OffsetDateTime odt) {
        return (odt != null) ? odt.toLocalDateTime() : null;
    }


    // LOCAL :  OFFSET

    private OffsetDateTime toUtc(LocalDateTime ldt) {
        return (ldt != null) ? ldt.atOffset(ZoneOffset.UTC) : null;
    }

    @Override
    public JobExecution toEntity(String jobName, StatusJob status) {
        return null;
    }


    // ENTITY :  RESPONSE DTO

    @Override
    public JobExecutionResponse toResponse(JobExecution entity) {
        if (entity == null) return null;

        return JobExecutionResponse.builder()
                .id(entity.getId())
                .status(entity.getStatus())
                .startTime(toLocal(entity.getStartTime())) // FIX
                .endTime(toLocal(entity.getEndTime()))     // FIX
                .errorType(entity.getErrorType())
                .errorMessage(entity.getErrorMessage())
                .build();
    }

    @Override
    public void updateEntity(JobExecution target, StatusJob status, String errorMessage) {

    }

    @Override
    public void updateEntity(JobExecution target, StatusJob status, StatusJobErrorType errorType, String errorMessage) {

    }


    // REQUEST DTO → ENTITY : solo per filtri

    @Override
    public JobExecution toEntity(JobExecutionRequest req) {
        if (req == null) return null;

        JobExecution e = new JobExecution();
        e.setStartTime(toUtc(req.getFrom()));  // FIX
        e.setEndTime(toUtc(req.getTo()));      // FIX
        return e;
    }
}