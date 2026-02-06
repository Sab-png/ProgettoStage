package it.spindox.stagelab.magazzino.services;

import it.spindox.stagelab.magazzino.dto.JobExecution.JobExecutionRequest;
import it.spindox.stagelab.magazzino.dto.JobExecution.JobExecutionResponse;
import it.spindox.stagelab.magazzino.entities.JobExecution;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface JobExecutionService {

    // =========================
    // API / Controller methods
    // =========================
    @Transactional(readOnly = true)
    JobExecutionResponse getById(Long id);

    @Transactional(readOnly = true)
    Page<JobExecutionResponse> search(JobExecutionRequest request);

    // =========================
    // Scheduler lifecycle
    // =========================
    JobExecution start();

    void success(JobExecution job);

    /** Per compatibilità: delega a failed(...) */
    void error(JobExecution job, Exception e);

    void failed(JobExecution job, Exception e);

    // =========================
    // Repository-style helpers
    // =========================
    @Transactional(readOnly = true)
    Optional<JobExecution> findLast();

    @Transactional(readOnly = true)
    Optional<JobExecution> findRunning();

    /**
     * Ricerca avanzata con filtri
     */
    @Transactional(readOnly = true)
    Page<JobExecution> search(
            StatusJob status,
            LocalDateTime startFrom,
            LocalDateTime startTo,
            Boolean hasError,
            Pageable pageable
    );
}