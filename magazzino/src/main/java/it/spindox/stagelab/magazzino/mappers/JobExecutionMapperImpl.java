package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.JobExecution.JobExecutionResponse;
import it.spindox.stagelab.magazzino.entities.JobExecution;
import it.spindox.stagelab.magazzino.entities.SJobErrorType;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;



@Slf4j
@Component
public class JobExecutionMapperImpl implements JobExecutionMapper {

    @Override
    public JobExecution toEntity(String jobName, StatusJob status) {
        JobExecution job = new JobExecution();
        job.setStatus(status);
        // Salviamo in UTC per consistenza
        job.setStartTime(LocalDateTime.now());
        return job;
    }

    @Override
    public JobExecutionResponse toResponse(JobExecution entity) {
        if (entity == null) return null;

        return JobExecutionResponse.builder()
                .id(entity.getId())
                .status(entity.getStatus())
                .startTime(entity.getStartTime() != null ? entity.getStartTime().toLocalDateTime() : null)
                .endTime(entity.getEndTime() != null ? entity.getEndTime().toLocalDateTime() : null)
                .errorType(entity.getErrorType())
                .errorMessage(entity.getErrorMessage())
                .build();
    }

    @Override
    public void updateEntity(JobExecution target, StatusJob status, String errorMessage) {
        target.setStatus(status);
        target.setEndTime(LocalDateTime.now());
        target.setErrorMessage(errorMessage);
    }

    @Override
    public void updateEntity(JobExecution target, StatusJob status, SJobErrorType errorType, String errorMessage) {
        target.setStatus(status);
        target.setEndTime(LocalDateTime.now());
        target.setErrorType(errorType);
        target.setErrorMessage(errorMessage);
    }
}
