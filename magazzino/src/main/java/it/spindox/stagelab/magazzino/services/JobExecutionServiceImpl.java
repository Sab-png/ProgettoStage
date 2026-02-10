package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.JobExecution.JobExecutionRequest;
import it.spindox.stagelab.magazzino.dto.JobExecution.JobExecutionResponse;
import it.spindox.stagelab.magazzino.entities.JobExecution;
import it.spindox.stagelab.magazzino.entities.SJobErrorType;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import it.spindox.stagelab.magazzino.exceptions.ResourceNotFoundException;
import it.spindox.stagelab.magazzino.mappers.JobExecutionMapper;
import it.spindox.stagelab.magazzino.repositories.JobExecutionRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final JobExecutionMapper jobExecutionMapper;


    // API


    @Override
    @Transactional(readOnly = true)
    public JobExecutionResponse getById(Long id) {
        JobExecution job = jobExecutionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("JobExecution not found: " + id));

        return jobExecutionMapper.toResponse(job);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobExecutionResponse> search(JobExecutionRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<JobExecution> page = jobExecutionRepository.search(
                request.getStatus(),
                request.getStartFrom(),
                request.getStartTo(),
                request.getHasError(),
                pageable
        );

        return page.map(jobExecutionMapper::toResponse);
    }


    // Job lifecycle


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


    // Repository helpers


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
        return jobExecutionRepository.search(status, startFrom, startTo, hasError, pageable);
    }


    // ERROR TYPE MAPPER


    private SJobErrorType mapErrorType(Exception e) {

        if (e == null) {
            return SJobErrorType.UNKNOWN;
        }

        // VALIDATION
        if (e instanceof IllegalArgumentException ||
                e instanceof jakarta.validation.ValidationException) {
            return SJobErrorType.VALIDATION_ERROR;
        }

        // CONFIGURATION
        if (e instanceof IllegalStateException ||
                (e.getMessage() != null && e.getMessage().toLowerCase().contains("configuration"))) {
            return SJobErrorType.CONFIGURATION_ERROR;
        }

        // TECHNICAL (DB, rete, IO, timeout)
        if (e instanceof java.sql.SQLException ||
                e instanceof org.springframework.dao.DataAccessException ||
                e instanceof java.net.SocketTimeoutException ||
                e instanceof java.io.IOException) {
            return SJobErrorType.TECHNICAL_ERROR;
        }

        // EXTERNAL SERVICES (RestTemplate, WebClient, Feign)
        if (e instanceof org.springframework.web.client.RestClientException
                || e.getClass().getName().startsWith("feign.")) {
            return SJobErrorType.EXTERNAL_SERVICE;
        }

        // SECURITY
        if (e instanceof org.springframework.security.core.AuthenticationException ||
                e instanceof org.springframework.security.access.AccessDeniedException) {
            return SJobErrorType.SECURITY_ERROR;
        }

        // INTERRUPTED
        if (e instanceof InterruptedException) {
            Thread.currentThread().interrupt();
            return SJobErrorType.INTERRUPTED;
        }

        // QUALSIASI ALTRO TIPO DI ERRORE
        return SJobErrorType.SYSTEM_ERROR;
    }
}