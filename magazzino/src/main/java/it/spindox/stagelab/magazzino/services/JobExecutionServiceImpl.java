package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.jobExecution.JobExecutionRequest;
import it.spindox.stagelab.magazzino.dto.jobExecution.JobExecutionResponse;
import it.spindox.stagelab.magazzino.entities.JobExecution;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import it.spindox.stagelab.magazzino.entities.StatusJobErrorType;
import it.spindox.stagelab.magazzino.exceptions.ResourceNotFoundException;
import it.spindox.stagelab.magazzino.repositories.JobExecutionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import org.springframework.data.domain.*;




@Slf4j
@Service
@RequiredArgsConstructor
public class JobExecutionServiceImpl implements JobExecutionService {

    private final JobExecutionRepository repository;


    // UTILITIES PER CONVERSIONE DATE E MAPPING


    private static OffsetDateTime toUtc(LocalDateTime ldt) {
        return (ldt == null) ? null : ldt.atOffset(ZoneOffset.UTC);
    }

    private static LocalDateTime toLocal(OffsetDateTime odt) {
        return (odt == null) ? null : odt.toLocalDateTime();
    }

    private static OffsetDateTime nowUtc() {
        return OffsetDateTime.now(ZoneOffset.UTC);
    }

    private static String truncate(String msg) {
        if (msg == null) return null;
        return msg.length() > 1000 ? msg.substring(0, 1000) : msg;
    }

    private static JobExecutionResponse toResponse(JobExecution e) {
        if (e == null) return null;
        return JobExecutionResponse.builder()
                .id(e.getId())
                .status(e.getStatus())
                .startTime(toLocal(e.getStartTime()))
                .endTime(toLocal(e.getEndTime()))
                .errorType(e.getErrorType())
                .errorMessage(e.getErrorMessage())
                .build();
    }

    // GET BY ID

    @Override
    public JobExecutionResponse getById(Long id) {
        if (id == null) throw new IllegalArgumentException("id nullo");
        JobExecution e = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobExecution non trovato: id=" + id));
        return toResponse(e);
    }

    // SEARCH CON DTO

    @Override
    public Page<JobExecutionResponse> search(JobExecutionRequest request) {
        if (request == null) return Page.empty();

        // stato come enum (accetta null)

        StatusJob status = null;
        if (request.getStato() != null && !request.getStato().isBlank()) {
            status = StatusJob.valueOf(request.getStato().toUpperCase().trim());
        }

        Pageable pageable = PageRequest.of(
                Math.max(request.getPage(), 0),
                Math.max(request.getSize(), 1),
                Sort.by(Sort.Direction.ASC, "startTime")
        );

        OffsetDateTime from = toUtc(request.getFrom());
        OffsetDateTime to   = toUtc(request.getTo());

        Page<JobExecution> page = repository.search(status, from, to, request.getHasError(), pageable);
        return page.map(JobExecutionServiceImpl::toResponse);
    }

    // SEARCH IDS (DTO)

    @Override
    public Page<Long> searchIds(JobExecutionRequest req) {
        if (req == null) return Page.empty();

        StatusJob status = null;
        if (req.getStato() != null && !req.getStato().isBlank()) {
            status = StatusJob.valueOf(req.getStato().toUpperCase().trim());
        }

        Pageable pageable = PageRequest.of(
                Math.max(req.getPage(), 0),
                Math.max(req.getSize(), 1),
                Sort.by(Sort.Direction.DESC, "startTime")
        );

        OffsetDateTime from = toUtc(req.getFrom());
        OffsetDateTime to   = toUtc(req.getTo());

        return repository.searchIds(status, from, to, req.getHasError(), pageable);
    }

    // GET ALL PAGED

    @Override
    public Page<JobExecutionResponse> getAllPaged(int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), Sort.by("startTime").descending());
        return repository.findAll(pageable).map(JobExecutionServiceImpl::toResponse);
    }

    // FIND LAST

    @Override
    public Optional<JobExecution> findLast() {
        return repository.findFirstByOrderByStartTimeDesc();
    }

    // FIND RUNNING

    @Override
    public Optional<JobExecution> findRunning() {
        return repository.findFirstByStatus(StatusJob.RUNNING);
    }

    // SEARCH "raw"

    @Override
    public Page<JobExecution> search(StatusJob status, OffsetDateTime startFrom, OffsetDateTime startTo, Boolean hasError, Pageable pageable) {
        return repository.search(status, startFrom, startTo, hasError, pageable);
    }


    //  CICLO E FUNZIONAMENTO BASE DEL JOB :  START → RUNNING → SUCCESS / FAILED


    // START: crea la JobExecution IS RUNNING

    @Override
    public JobExecution start() {
        JobExecution job = new JobExecution();
        job.setStatus(StatusJob.RUNNING);
        job.setStartTime(nowUtc());
        job.setEndTime(null);
        job.setErrorType(null);
        job.setErrorMessage(null);
        return repository.save(job);
    }

    // SUCCESS: chiude il job con esito positivo

    @Override
    public void success(JobExecution job) {
        if (job == null) throw new IllegalArgumentException("JobExecution nullo in success()");
        job.setStatus(StatusJob.SUCCESS);
        job.setEndTime(nowUtc());
        job.setErrorType(null);
        job.setErrorMessage(null);
        repository.save(job);
    }

    // FAILED con dettaglio del tipo errore + eccezione

    @Override
    public void failed(JobExecution job, StatusJobErrorType errorType, Exception e) {
        if (job == null) throw new IllegalArgumentException("JobExecution nullo in failed(job, type, e)");
        job.setStatus(StatusJob.FAILED);
        job.setEndTime(nowUtc());
        job.setErrorType(errorType);
        job.setErrorMessage(truncate(e != null ? e.getMessage() : null));
        repository.save(job);
    }

    // FAILED overload senza type

    @Override
    public void failed(JobExecution job, Exception e) {
        if (job == null) throw new IllegalArgumentException("JobExecution nullo in failed(job, e)");
        job.setStatus(StatusJob.FAILED);
        job.setEndTime(nowUtc());
        job.setErrorType(null);
        job.setErrorMessage(truncate(e != null ? e.getMessage() : null));
        repository.save(job);
    }
}