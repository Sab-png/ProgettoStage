package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.JobExecution.JobExecutionRequest;
import it.spindox.stagelab.magazzino.dto.JobExecution.JobExecutionResponse;
import it.spindox.stagelab.magazzino.entities.JobExecution;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import it.spindox.stagelab.magazzino.repositories.JobExecutionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobExecutionServiceImpl implements JobExecutionService {

    private final JobExecutionRepository jobExecutionRepository;

    // =========================
    // API / CONTROLLER
    // =========================

    @Override
    @Transactional(readOnly = true)
    public JobExecutionResponse getById(Long id) {
        return jobExecutionRepository.findById(id)
                .map(this::toResponse)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobExecutionResponse> search(JobExecutionRequest request) {
        // Implementazione base: se vuoi, qui puoi mappare i filtri del request verso la query avanzata
        // Esempio minimal: ritorna tutto non paginato e mappato a response
        return jobExecutionRepository
                .findAll(Pageable.unpaged())
                .map(this::toResponse);
    }

    // =========================
    // SCHEDULER LIFECYCLE
    // =========================

    @Override
    @Transactional
    public JobExecution start() {
        JobExecution job = new JobExecution();
        job.setStatus(StatusJob.RUNNING);
        job.setStartTime(LocalDateTime.now());

        jobExecutionRepository.save(job);

        log.info("JOB START | id={}", job.getId());
        return job;
    }

    @Override
    @Transactional
    public void success(JobExecution job) {
        job.setStatus(StatusJob.SUCCESS);
        job.setEndTime(LocalDateTime.now());

        jobExecutionRepository.save(job);

        log.info("JOB SUCCESS | id={}", job.getId());
    }

    @Override
    @Transactional
    public void error(JobExecution job, Exception e) {
        // Allinea "error" a "failed" (se non distingui stati diversi)
        failed(job, e);
    }

    @Override
    @Transactional
    public void failed(JobExecution job, Exception e) {
        job.setStatus(StatusJob.FAILED);
        job.setEndTime(LocalDateTime.now());
        job.setErrorMessage(e != null ? e.getMessage() : null);

        jobExecutionRepository.save(job);

        log.error("JOB FAILED | id={}", job.getId(), e);
    }

    // =========================
    // REPOSITORY HELPERS
    // =========================

    @Override
    @Transactional(readOnly = true)
    public Optional<JobExecution> findLast() {
        return jobExecutionRepository.findFirstByOrderByStartTimeDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<JobExecution> findRunning() {
        return jobExecutionRepository.findFirstByStatus(StatusJob.RUNNING);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobExecution> search(
            StatusJob status,
            LocalDateTime startFrom,
            LocalDateTime startTo,
            Boolean hasError,
            Pageable pageable
    ) {
        // Delega alla query avanzata del repository (se definita)
        return jobExecutionRepository.search(status, startFrom, startTo, hasError, pageable);
    }

    // =========================
    // MAPPER INTERNO
    // =========================

    private JobExecutionResponse toResponse(JobExecution entity) {
        JobExecutionResponse r = new JobExecutionResponse();
        r.setId(entity.getId());
        r.setStatus(entity.getStatus());
        r.setStartTime(entity.getStartTime());
        r.setEndTime(entity.getEndTime());
        r.setErrorMessage(entity.getErrorMessage());
        return r;
    }
}