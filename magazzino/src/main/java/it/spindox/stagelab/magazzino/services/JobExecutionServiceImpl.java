package it.spindox.stagelab.magazzino.services;

import it.spindox.stagelab.magazzino.dto.JobExecution.JobExecutionRequest;
import it.spindox.stagelab.magazzino.dto.JobExecution.JobExecutionResponse;
import it.spindox.stagelab.magazzino.entities.JobExecution;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import it.spindox.stagelab.magazzino.repositories.JobExecutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobExecutionServiceImpl implements JobExecutionService {

    private final JobExecutionRepository repository;

    // API / Controller methods


    @Override
    @Transactional(readOnly = true)
    public JobExecutionResponse getById(Long id) {
        JobExecution entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("JobExecution non trovata: id=" + id));
        return toResponse(entity);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<JobExecutionResponse> search(JobExecutionRequest request) {
        // Estraggo parametri dal DTO adattando i nomi
        StatusJob status = request.getStatus();
        LocalDateTime startFrom = request.getStartFrom();
        LocalDateTime startTo = request.getStartTo();
        Boolean hasError = request.getHasError();

        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() != null ? request.getSize() : 10;
        Pageable pageable = PageRequest.of(page, size);

        Page<JobExecution> result = repository.search(status, startFrom, startTo, hasError, pageable);
        return result.map(this::toResponse);
    }


    // Scheduler lifecycle


    @Override
    @Transactional
    public JobExecution start() {
        JobExecution job = new JobExecution();
        job.setStatus(StatusJob.RUNNING);
        job.setStartTime(LocalDateTime.now());
        return repository.save(job);
    }

    @Override
    @Transactional
    public void success(JobExecution job) {
        job.setStatus(StatusJob.SUCCESS);
        job.setEndTime(LocalDateTime.now());
        repository.save(job);
    }

    @Override
    @Transactional
    public void error(JobExecution job, Exception e) {
        job.setStatus(StatusJob.ERROR);
        job.setEndTime(LocalDateTime.now());
        job.setErrorMessage(e != null ? e.getMessage() : "Errore non specificato");
        repository.save(job);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<JobExecution> findLast() {
        return repository.findFirstByOrderByStartTimeDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<JobExecution> findRunning() {
        return repository.findRunning();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobExecution> search(StatusJob status,
                                     LocalDateTime startFrom,
                                     LocalDateTime startTo,
                                     Boolean hasError,
                                     Pageable pageable) {
        return repository.search(status, startFrom, startTo, hasError, pageable);
    }

    // Mapper minimale


    private JobExecutionResponse toResponse(JobExecution entity) {
        JobExecutionResponse dto = new JobExecutionResponse();
        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setErrorMessage(entity.getErrorMessage());
        return dto;
    }
}