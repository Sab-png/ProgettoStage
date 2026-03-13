package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.jobExecution.DtoJobRequest;
import it.spindox.stagelab.magazzino.dto.jobExecution.DtoJobResponse;
import it.spindox.stagelab.magazzino.entities.JobExecution;
import it.spindox.stagelab.magazzino.entities.StatusJobErrorType;
import it.spindox.stagelab.magazzino.entities.StatusJob;

public interface JobExecutionMapper {


    // Crea una nuova JobExecution all'avvio del job

    JobExecution toEntity(String jobName, StatusJob status);


    // Converte entity -> response

    DtoJobResponse toResponse(JobExecution entity);


    // Aggiorna una JobExecution esistente (es. SUCCESS / ERROR)

    void updateEntity(JobExecution target,
                      StatusJob status,
                      String errorMessage);

    void updateEntity(JobExecution target,
                      StatusJob status,
                      StatusJobErrorType errorType,
                      String errorMessage);

    // REQUEST DTO → ENTITY (solo per filtri)

    JobExecution toEntity(DtoJobRequest req);
}
