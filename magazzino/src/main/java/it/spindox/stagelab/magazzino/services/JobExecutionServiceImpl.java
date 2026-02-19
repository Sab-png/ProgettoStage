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

    private static OffsetDateTime nowUtc() {
        return OffsetDateTime.now(ZoneOffset.UTC);
    }

    private static String truncate(String msg) {
        if (msg == null) return null;
        return msg.length() > 1000 ? msg.substring(0, 1000) : msg;
    }

    // GET BY ID

    @Override

    public JobExecutionResponse getById(Long id) {
        log.info("Richiesta JobExecution per id={}", id);

        if (id == null) {
            log.warn("ID nullo passato a getById(): ritorno null");
            return null;
        }

        return null;
    }

    // SEARCH

    @Override

    public Page<JobExecutionResponse> search(JobExecutionRequest request) {
        log.info("Ricerca JobExecution avviata");

        if (request == null) {
            log.warn("JobExecutionRequest è NULL in search(): ritorno pagina vuota");
            return Page.empty();
        }

        log.debug("Parametri ricerca -> status={}, from={}, to={}, page={}, size={}",
                request.getStatus(),
                request.getFrom(),
                request.getTo(),
                request.getPage(),
                request.getSize()
        );

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

    // JOB SUCCESS
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

    // JOB FAILED (con error type)
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

    // FIND LAST

    @Override
    @Transactional

    public Optional<JobExecution> findLast() {
        log.info("Richiesta ultimo JobExecution");

        return Optional.empty();
    }

    // FIND RUNNING

    @Override
    @Transactional

    public Optional<JobExecution> findRunning() {
        log.info("Ricerca JobExecution in stato RUNNING");

        return Optional.empty();
    }

    // SEARCH ENTITY (no DTO)
    @Override
    @Transactional

    public Page<JobExecution> search(StatusJob status, OffsetDateTime startFrom, OffsetDateTime startTo, Boolean hasError, Pageable pageable) {
        log.info("Ricerca JobExecution con parametri raw");

        log.debug("status={}, from={}, to={}, hasError={}, pageable={}",
                status, startFrom, startTo, hasError, pageable);

        return null;
    }

    // FAILED
    @Override
    @Transactional

    public void failed(JobExecution job, Exception e) {
        if (job == null) {
            log.error("JobExecution è NULL durante failed(...). Eccezione: {}",
                    e != null ? e.getMessage() : "null", e);
            return;
        }

        log.error("[JOB FAILED] id={} msg={}",
                job.getId(),
                e != null ? e.getMessage() : "Errore sconosciuto",
                e);
    }

    // SEARCH IDS
    @Override
    @Transactional

    public Page<Long> searchIds(JobExecutionRequest req) {
        log.info("Ricerca ID JobExecution avviata");

        if (req == null) {
            log.warn("JobExecutionRequest è NULL in searchIds(): ritorno pagina vuota");
            return Page.empty();
        }

        log.debug("Parametri ricerca ID -> from={}, to={}, status={}, page={}, size={}",
                req.getFrom(),
                req.getTo(),
                req.getStatus(),
                req.getPage(),
                req.getSize()
        );
        return null;
    }

    // GET ALL PAGED
    @Override
    @Transactional

    public Page<JobExecutionResponse> getAllPaged(int page, int size) {
        log.info("getAllPaged: page={}, size={}", page, size);

        PageRequest pr = PageRequest.of(page, size);
        log.debug("PageRequest creato: {}", pr);

        return null;
    }
}
