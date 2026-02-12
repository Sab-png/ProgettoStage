package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.JobExecution.JobExecutionRequest;
import it.spindox.stagelab.magazzino.dto.JobExecution.JobExecutionResponse;
import it.spindox.stagelab.magazzino.entities.JobExecution;
import it.spindox.stagelab.magazzino.entities.SJobErrorType;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.time.OffsetDateTime;


public interface JobExecutionService {

    // =====================
    //  API: CONTROLLER
    // =====================

    @Transactional(readOnly = true)
    JobExecutionResponse getById(Long id);

    @Transactional(readOnly = true)
    Page<JobExecutionResponse> search(JobExecutionRequest request);


    // =====================
    //  JOB LIFECYCLE
    // =====================

    /**
     * Crea un nuovo JobExecution con status RUNNING
     */
    JobExecution start();

    /**
     * Marca il job come completato con successo
     */
    void success(JobExecution job);

    /**
     * Marca il job come fallito (VERSIONE CORRETTA)
     */
    void failed(JobExecution job, SJobErrorType errorType, Exception e);


    // =====================
    //  REPOSITORY HELPERS
    // =====================

    @Transactional(readOnly = true)
    Optional<JobExecution> findLast();

    @Transactional(readOnly = true)
    Optional<JobExecution> findRunning();

    /**
     * Ricerca interna (non per controller)
     */
    @Transactional(readOnly = true)
    Page<JobExecution> search(
            StatusJob status,
            OffsetDateTime startFrom,
            OffsetDateTime startTo,
            Boolean hasError,
            Pageable pageable
    );


    void failed(JobExecution job, Exception e);

    Page<Long> searchIds(JobExecutionRequest req);
}