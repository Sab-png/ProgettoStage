package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.JobExecution.JobExecutionRequest;
import it.spindox.stagelab.magazzino.dto.JobExecution.JobExecutionResponse;
import it.spindox.stagelab.magazzino.entities.JobExecution;
import it.spindox.stagelab.magazzino.entities.SJobErrorType;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;


public interface JobExecutionService {


    // API

    @Transactional(readOnly = true)
    JobExecutionResponse getById(Long id);

    @Transactional(readOnly = true)
    Page<JobExecutionResponse> search(JobExecutionRequest request);


    // Job lifecycle

    JobExecution start();

    void success(JobExecution job);

    /**
     * legacy
     */
    void error(JobExecution job, Exception e);

    /**
     * legacy
     */
    void failed(JobExecution job, Exception e);

    /**
     * nuovo e corretto
     */
    void failed(JobExecution job, SJobErrorType errorType, Exception e);


    // Repository helpers

    @Transactional(readOnly = true)
    Optional<JobExecution> findLast();

    @Transactional(readOnly = true)
    Optional<JobExecution> findRunning();

    @Transactional(readOnly = true)
    Page<JobExecution> search(
            StatusJob status,
            LocalDateTime startFrom,
            LocalDateTime startTo,
            Boolean hasError,
            Pageable pageable
    );
}
