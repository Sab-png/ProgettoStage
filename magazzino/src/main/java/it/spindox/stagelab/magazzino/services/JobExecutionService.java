package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.jobExecution.DtoJobRequest;
import it.spindox.stagelab.magazzino.dto.jobExecution.DtoJobResponse;
import it.spindox.stagelab.magazzino.entities.JobExecution;
import it.spindox.stagelab.magazzino.entities.StatusJobErrorType;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.time.OffsetDateTime;


public interface JobExecutionService {


    @Transactional(readOnly = true)
    DtoJobResponse getById(Long id);

    @Transactional(readOnly = true)
    Page<DtoJobResponse> search(DtoJobRequest request);



      // Crea un nuovo JobExecution con status RUNNING

    JobExecution start();


     // Segna il job come completato con successo

    void success(JobExecution job);


      // Segna il job come fallito con errore specifico

    void failed(JobExecution job, StatusJobErrorType errorType, Exception e);

    @Transactional(readOnly = true)
    Optional<JobExecution> findLast();

    @Transactional(readOnly = true)
    Optional<JobExecution> findRunning();


     // Ricerca di Page Job execution

    @Transactional(readOnly = true)
    Page<JobExecution> search(
            StatusJob status,
            OffsetDateTime startFrom,
            OffsetDateTime startTo,
            Boolean hasError,
            Pageable pageable
    );


    void failed(JobExecution job, Exception e);

    Page<Long> searchIds(DtoJobRequest req);

    Page<DtoJobResponse> getAllPaged(@Min(0) int page, @Min(1) int size);
}