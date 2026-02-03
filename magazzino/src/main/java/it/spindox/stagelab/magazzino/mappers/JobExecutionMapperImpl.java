package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.JobExecution.JobExecutionResponse;
import it.spindox.stagelab.magazzino.entities.JobExecution;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class JobExecutionMapperImpl implements JobExecutionMapper {

    @Override
    public JobExecution toEntity(String jobName, StatusJob status) {
        JobExecution job = new JobExecution();
        job.setStatus(status);
        job.setStartTime(LocalDateTime.now());
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
        response.setStartTime(entity.getStartTime());
        response.setEndTime(entity.getEndTime());
        response.setErrorMessage(entity.getErrorMessage());

        return response;
    }

    @Override
    public void updateEntity(JobExecution target,
                             StatusJob status,
                             String errorMessage) {

        target.setStatus(status);
        target.setEndTime(LocalDateTime.now());

        if (errorMessage != null) {
            target.setErrorMessage(errorMessage);
        }
    }
}
