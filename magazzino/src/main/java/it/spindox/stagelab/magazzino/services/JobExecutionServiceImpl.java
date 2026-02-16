package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.jobExecution.JobExecutionRequest;
import it.spindox.stagelab.magazzino.dto.jobExecution.JobExecutionResponse;
import it.spindox.stagelab.magazzino.entities.JobExecution;
import it.spindox.stagelab.magazzino.entities.StatusJobErrorType;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import it.spindox.stagelab.magazzino.mappers.JobExecutionMapper;
import it.spindox.stagelab.magazzino.repositories.JobExecutionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.springframework.data.domain.*;




@Slf4j
@Service
@RequiredArgsConstructor
public class JobExecutionServiceImpl implements JobExecutionService {

    private final JobExecutionRepository jobExecutionRepository;
    private final JobExecutionMapper jobExecutionMapper;


    // GET BY ID

    @Override
    @Transactional(readOnly = true)
    public JobExecutionResponse getById(Long id) {
        JobExecution job = jobExecutionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("JobExecution not found: " + id));

        return jobExecutionMapper.toResponse(job);
    }


    // SEARCH con DTO

    @Override
    @Transactional(readOnly = true)
    public Page<JobExecutionResponse> search(JobExecutionRequest request) {

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        StatusJob status = parseStatus(request.getStato());
        LocalDateTime from = safeToLocalDateTime(request.getFrom());
        LocalDateTime to   = safeToLocalDateTime(request.getTo());
        Boolean hasError   = request.getHasError();

        Page<JobExecution> result = jobExecutionRepository.search(
                status,
                from,
                to,
                hasError,
                pageable
        );

        return result.map(jobExecutionMapper::toResponse);
    }


    // START JOB

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public JobExecution start() {
        JobExecution job = new JobExecution();
        job.setStatus(StatusJob.RUNNING);
        job.setStartTime(LocalDateTime.now());

        jobExecutionRepository.save(job);

        log.info("JOB START | id={}", job.getId());
        return job;
    }


    // SUCCESS

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void success(JobExecution job) {
        job.setStatus(StatusJob.SUCCESS);
        job.setEndTime(LocalDateTime.now());
        jobExecutionRepository.save(job);
    }


    // FAILED con tipo errore esplicito

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void failed(JobExecution job, StatusJobErrorType errorType, Exception e) {
        job.setStatus(StatusJob.FAILED);
        job.setEndTime(LocalDateTime.now());
        job.setErrorType(errorType);
        job.setErrorMessage(e != null ? e.getMessage() : null);
        jobExecutionRepository.save(job);
    }


    // FAILED fallback :  UNKNOWN

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void failed(JobExecution job, Exception e) {
        failed(job, StatusJobErrorType.UNKNOWN, e);
    }


    // FIND LAST JOB

    @Override
    @Transactional(readOnly = true)
    public Optional<JobExecution> findLast() {
        return jobExecutionRepository.findFirstByOrderByStartTimeDesc();
    }


    // FIND RUNNING JOB

    @Override
    @Transactional(readOnly = true)
    public Optional<JobExecution> findRunning() {
        return jobExecutionRepository.findFirstByStatus(StatusJob.RUNNING);
    }


    // SEARCH “interno” :  NON DTO
    // --------------------------
    @Override
    @Transactional(readOnly = true)
    public Page<JobExecution> search(
            StatusJob status,
            OffsetDateTime startFrom,
            OffsetDateTime startTo,
            Boolean hasError,
            Pageable pageable) {

        LocalDateTime from = safeToLocalDateTime(startFrom);
        LocalDateTime to   = safeToLocalDateTime(startTo);

        return jobExecutionRepository.search(
                status,
                from,
                to,
                hasError,
                pageable
        );
    }


    // SEARCH IDs (USATO DA GET /jobs)

    @Override
    @Transactional(readOnly = true)
    public Page<Long> searchIds(JobExecutionRequest req) {

        Pageable pageable = PageRequest.of(
                req.getPage() != null ? req.getPage() : 0,
                req.getSize() != null ? req.getSize() : 10
        );

        StatusJob status = parseStatus(req.getStato());
        LocalDateTime from = safeToLocalDateTime(req.getFrom());
        LocalDateTime to   = safeToLocalDateTime(req.getTo());
        Boolean hasError   = req.getHasError();

        return jobExecutionRepository.searchIds(
                status,
                from,
                to,
                hasError,
                pageable
        );
    }


    // GET ALL PAGED : UTILIZZATO  DA /jobs/list

    @Override
    @Transactional(readOnly = true)
    public Page<JobExecutionResponse> getAllPaged(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<JobExecution> result = jobExecutionRepository.findAll(pageable);

        return result.map(jobExecutionMapper::toResponse);
    }


    // HELPER PER LE CONVERSIONI


    private static LocalDateTime safeToLocalDateTime(LocalDateTime dt) {
        return dt != null ? dt : null;
    }

    private static LocalDateTime safeToLocalDateTime(OffsetDateTime dt) {
        return dt != null ? dt.toLocalDateTime() : null;
    }

    private static StatusJob parseStatus(String stato) {
        if (stato == null || stato.isBlank()) return null;
        try {
            return StatusJob.valueOf(stato.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // oppure puo' lanciare un new ValidationException
        }
    }
}