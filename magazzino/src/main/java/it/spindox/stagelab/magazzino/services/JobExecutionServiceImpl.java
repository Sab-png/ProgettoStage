package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.JobExecution.JobExecutionRequest;
import it.spindox.stagelab.magazzino.dto.JobExecution.JobExecutionResponse;
import it.spindox.stagelab.magazzino.entities.JobExecution;
import it.spindox.stagelab.magazzino.entities.SJobErrorType;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import it.spindox.stagelab.magazzino.exceptions.ResourceNotFoundException;
import it.spindox.stagelab.magazzino.repositories.JobExecutionRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class JobExecutionServiceImpl implements JobExecutionService {

    private final JobExecutionRepository jobExecutionRepository;

    @Override
    public JobExecutionResponse getById(Long id) {
        return null;
    }

    @Override
    public Page<JobExecutionResponse> search(JobExecutionRequest request) {
        return null;
    }

    @Override
    @Transactional
    public JobExecution start() {
        JobExecution job = new JobExecution();
        job.setStatus(StatusJob.RUNNING);
        job.setStartTime(OffsetDateTime.now());

        jobExecutionRepository.save(job);

        log.info("JOB START | id={}", job.getId());
        return job;
    }

    @Override
    @Transactional
    public void success(JobExecution job) {
        job.setStatus(StatusJob.SUCCESS);
        job.setEndTime(OffsetDateTime.now());

        jobExecutionRepository.save(job);

        log.info("JOB SUCCESS | id={}", job.getId());
    }

    @Override
    @Transactional
    public void error(JobExecution job, Exception e) {
        failed(job, mapErrorType(e), e);
    }

    @Override
    @Transactional
    public void failed(JobExecution job, Exception e) {
        failed(job, mapErrorType(e), e);
    }

    @Override
    @Transactional
    public void failed(JobExecution job, SJobErrorType errorType, Exception e) {
        job.setStatus(StatusJob.FAILED);
        job.setEndTime(OffsetDateTime.now());
        job.setErrorType(errorType != null ? errorType : SJobErrorType.UNKNOWN);
        job.setErrorMessage(e != null ? e.getMessage() : null);

        jobExecutionRepository.save(job);

        log.error(
                "JOB FAILED | id={} | errorType={}",
                job.getId(),
                job.getErrorType(),
                e
        );
    }

    @Override
    public Optional<JobExecution> findLast() {
        return Optional.empty();
    }

    @Override
    public Optional<JobExecution> findRunning() {
        return Optional.empty();
    }

    @Override
    public Page<JobExecution> search(StatusJob status, LocalDateTime startFrom, LocalDateTime startTo, Boolean hasError, Pageable pageable) {
        return null;
    }

    // =========================
    // ERROR TYPE MAPPER
    // =========================

    private SJobErrorType mapErrorType(Exception e) {

        if (e == null) {
        }
        return null;
    }
}
