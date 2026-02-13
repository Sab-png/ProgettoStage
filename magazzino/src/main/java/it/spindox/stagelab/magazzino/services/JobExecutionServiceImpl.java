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


@Slf4j
@Service
@RequiredArgsConstructor
public class JobExecutionServiceImpl implements JobExecutionService {

    private final JobExecutionRepository jobExecutionRepository;
    private final JobExecutionMapper jobExecutionMapper;


     // Recupera un JobExecution dal DB tramite ID.
     // Usa readOnly perché è una semplice operazione di lettura.

    @Override
    @Transactional(readOnly = true)
    public JobExecutionResponse getById(Long id) {
        JobExecution job = jobExecutionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("JobExecution not found: " + id));

        return jobExecutionMapper.toResponse(job);
    }


     // Ricerca con filtro avanzato via DTO (status, date, errori, paginazione).
    // solo lettura

    @Override
    @Transactional(readOnly = true)
    public Page<JobExecutionResponse> search(JobExecutionRequest request) {

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<JobExecution> result = jobExecutionRepository.search(
                request.getStatus(),
                request.getStartFrom(),
                request.getStartTo(),
                request.getHasError(),
                pageable
        );

        return result.map(jobExecutionMapper::toResponse);
    }


     // CREA un nuovo job in stato RUNNING.
     // Propagation = REQUIRES_NEW : va a creare sempre una nuova transazione indipendente
    // Questo garantisce che il record venga salvato anche se la logica schedule fallisce.

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public JobExecution start() {
        JobExecution job = new JobExecution();
        job.setStatus(StatusJob.RUNNING);                   // il job parte in RUNNING
        job.setStartTime(LocalDateTime.now());              // timestamp inizio

        jobExecutionRepository.save(job);                   // salvataggio nel DB
        log.info("JOB START | id={}", job.getId());
        return job;
    }


     // Segna un job come SUCCESS.
     // Per evitare rollback: si usa  REQUIRES_NEW.


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void success(JobExecution job) {
        job.setStatus(StatusJob.SUCCESS);
        job.setEndTime(LocalDateTime.now());                // timestamp fine
        jobExecutionRepository.save(job);
    }


     // Segna un job come FAILED con un tipo errore specifico.
     // Registra: stato, data fine, tipo errore, messaggio.


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void failed(JobExecution job, StatusJobErrorType errorType, Exception e) {
        job.setStatus(StatusJob.FAILED);
        job.setEndTime(LocalDateTime.now());                // timestamp fine
        job.setErrorType(errorType);
        job.setErrorMessage(e != null ? e.getMessage() : null);
        jobExecutionRepository.save(job);
    }


     //  Ritorna l'ultimo job ordinato per start time (il più recente)

    @Override
    @Transactional(readOnly = true)
    public Optional<JobExecution> findLast() {
        return jobExecutionRepository.findFirstByOrderByStartTimeDesc();
    }


     // Ritorna eventuale job ancora in RUNNING
      // Serve allo scheduler per evitare esecuzioni parallele


    @Override
    @Transactional(readOnly = true)
    public Optional<JobExecution> findRunning() {
        return jobExecutionRepository.findFirstByStatus(StatusJob.RUNNING);
    }


      // Versione interna della ricerca (non via DTO).
     // Delegata al repository con conversione delle date.


    @Override
    @Transactional(readOnly = true)
    public Page<JobExecution> search(
            StatusJob status,
            OffsetDateTime startFrom,
            OffsetDateTime startTo,
            Boolean hasError,
            Pageable pageable) {

        return jobExecutionRepository.search(
                status,
                startFrom.toLocalDateTime(),
                startTo.toLocalDateTime(),
                hasError,
                pageable
        );
    }


     // Gestione fallback del failed SENZA tipo errore esplicito.
     // Mappa automaticamente l'errore come UNKNOWN.
      // fallback = meccanismo tolleranza degli errori, che esegue un azione alternativa quando un operazione fallisce
      // per evitare di crashare il sistema


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void failed(JobExecution job, Exception e) {
        failed(job, StatusJobErrorType.UNKNOWN, e);
    }

    @Override
    public Page<Long> searchIds(JobExecutionRequest req) {
        return null;
    }
}
