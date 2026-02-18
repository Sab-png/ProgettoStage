package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.jobExecution.JobExecutionRequest;
import it.spindox.stagelab.magazzino.dto.jobExecution.JobExecutionResponse;
import it.spindox.stagelab.magazzino.entities.JobExecution;
import it.spindox.stagelab.magazzino.entities.StatusJobErrorType;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import it.spindox.stagelab.magazzino.repositories.JobExecutionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import org.springframework.data.domain.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class JobExecutionServiceImpl implements JobExecutionService {

    private final JobExecutionRepository repository;


    //  timestamp in UTC

    private static OffsetDateTime nowUtc() {
        return OffsetDateTime.now(ZoneOffset.UTC);
    }

    private static String truncate(String msg) {
        if (msg == null) return null;
        return msg.length() > 1000 ? msg.substring(0, 1000) : msg;
    }

    @Override
    public JobExecutionResponse getById(Long id) {
        return null;
    }

    @Override
    public Page<JobExecutionResponse> search(JobExecutionRequest request) {
        return null;
    }


    // CREATE NEW JOB

    @Override
    @Transactional
    public JobExecution start() {

        JobExecution job = new JobExecution();
        job.setStatus(StatusJob.RUNNING);
        job.setStartTime(nowUtc());
        job.setEndTime(null);
        job.setErrorType(null);
        job.setErrorMessage(null);

        job = repository.save(job);

        log.info("[JOB STARTED] id={} startTime(UTC)={}", job.getId(), job.getStartTime());
        return job;
    }

    //  JOB SUCCESS

    @Override
    @Transactional
    public void success(JobExecution job) {

        job.setStatus(StatusJob.SUCCESS);
        job.setEndTime(nowUtc());
        job.setErrorType(null);
        job.setErrorMessage(null);

        repository.save(job);

        log.info("[JOB SUCCESS] id={} endTime(UTC)={}", job.getId(), job.getEndTime());
    }


    // JOB FAILED

    @Override
    @Transactional
    public void failed(JobExecution job, StatusJobErrorType type, Exception e) {

        job.setStatus(StatusJob.FAILED);
        job.setEndTime(nowUtc());
        job.setErrorType(type);
        job.setErrorMessage(truncate(e != null ? e.getMessage() : null));

        repository.save(job);

        log.error("[JOB FAILED] id={} type={} msg={}",
                job.getId(), type, job.getErrorMessage(), e);
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
    public Page<JobExecution> search(StatusJob status, OffsetDateTime startFrom, OffsetDateTime startTo, Boolean hasError, Pageable pageable) {
        return null;
    }

    @Override
    public void failed(JobExecution job, Exception e) {

    }

    @Override
    public Page<Long> searchIds(JobExecutionRequest req) {
        return null;
    }

    @Override
    public Page<JobExecutionResponse> getAllPaged(int page, int size) {
        return null;
    }
}
