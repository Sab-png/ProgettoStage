package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.JobExecution.JobExecutionResponse;
import it.spindox.stagelab.magazzino.entities.JobExecution;
import it.spindox.stagelab.magazzino.entities.SJobErrorType;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Slf4j
@Component
public class JobExecutionMapperImpl implements JobExecutionMapper {

    @Override
    public JobExecution toEntity(String jobName, StatusJob status) {
        JobExecution job = new JobExecution();
        job.setStatus(status);
        job.setStartTime(OffsetDateTime.now());
        return job;
    }

    @Override
    public JobExecutionResponse toResponse(JobExecution entity) {
        if (entity == null) {
            return null;
        }

        JobExecutionResponse response = new JobExecutionResponse();
        response.setId(entity.getId());
        response.setStatus(entity.getStatus());
        response.setStartTime(entity.getStartTime().toLocalDateTime());
        response.setEndTime(entity.getEndTime().toLocalDateTime());
        response.setErrorType(entity.getErrorType());
        response.setErrorMessage(entity.getErrorMessage());

        return response;
    }

    @Override
    public void updateEntity(JobExecution target, StatusJob status, String errorMessage) {

    }

    @Override
    public void updateEntity(JobExecution target,
                             StatusJob status,
                             SJobErrorType errorType,
                             String errorMessage) {

        target.setStatus(status);
        target.setEndTime(OffsetDateTime.now());
        target.setErrorType(errorType);
        target.setErrorMessage(errorMessage);
    }
}