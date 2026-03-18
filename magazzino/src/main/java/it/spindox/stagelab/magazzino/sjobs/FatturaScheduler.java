package it.spindox.stagelab.magazzino.sjobs;
import it.spindox.stagelab.magazzino.entities.JobExecution;
import it.spindox.stagelab.magazzino.entities.SXFatturaJobexecutionErrorType;
import it.spindox.stagelab.magazzino.entities.StatusJobErrorType;
import it.spindox.stagelab.magazzino.exceptions.jobsexceptions.FatturaWorkExecutionException;
import it.spindox.stagelab.magazzino.exceptions.jobsexceptions.InvalidCapacityException;
import it.spindox.stagelab.magazzino.exceptions.jobsexceptions.InvalidFatturaException;
import it.spindox.stagelab.magazzino.exceptions.jobsexceptions.UnknownJobException;
import it.spindox.stagelab.magazzino.services.FatturaWorkExecutionService;
import it.spindox.stagelab.magazzino.services.JobExecutionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;





@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class FatturaScheduler {

    private static final ZoneId ZONE_ROME = ZoneId.of("Europe/Rome");
    private static final DateTimeFormatter TS =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withZone(ZONE_ROME);

    //  DIPENDENZE
    private final FatturaWorkExecutionService fatturaWorkExecutionService;
    private final JobExecutionService jobExecutionService;

    //  configuration properties
    @Value("${fatture.check.enabled:true}")
    private boolean fattureJobEnabled;

    @Value("${fatture.check.cron:0 */10 * * * *}")
    private String fattureCheckCron;

    //  ENTRY POINT SCHEDULER
    @Scheduled(cron = "${fatture.check.cron}", zone = "Europe/Rome")
    public void runFattureDailyCheck() {

        if (!fattureJobEnabled) {
            log.info("[FATTURE JOB] DISABILITATO via config (fatture.check.enabled=false)");
            return;
        }

        final Instant startInstant = Instant.now();
        log.info(" FATTURE JOB START  now={} cron='{}'",
                TS.format(startInstant), fattureCheckCron);

        JobExecution job = null;

        try {
            // 1) JOB RUNNING
            job = jobExecutionService.start();
            log.info("[JOB RUNNING] id={} startTime={}", job.getId(), job.getStartTime());

            // 2) LOGICA DEL JOB
            //    Controlla/aggiorna lo stato pagamento di tutte le fatture
            fatturaWorkExecutionService.paymentCheckAllFatture();

            // 3) JOB SUCCESS
            jobExecutionService.success(job);
            log.info("[FATTURE JOB SUCCESS] id={}", job.getId());

            // ECCEZIONI

        } catch (FatturaWorkExecutionException e) {

            SXFatturaJobexecutionErrorType type = e.getErrorType();

            if (type == SXFatturaJobexecutionErrorType.BUSINESS_WARNING) {
                log.warn("[BUSINESS WARNING] {}", e.getMessage(), e);
                handleFailure(job, SXFatturaJobexecutionErrorType.BUSINESS_WARNING, e);

            } else if (type == SXFatturaJobexecutionErrorType.BUSINESS_ERROR) {
                log.warn("[BUSINESS ERROR] {}", e.getMessage(), e);
                handleFailure(job, SXFatturaJobexecutionErrorType.BUSINESS_ERROR, e);
            }

        } catch (InvalidCapacityException e) {
            log.error("[INVALID CAPACITY] {}", e.getMessage(), e);
            handleFailure(job, StatusJobErrorType.SYSTEM_ERROR, e);

        } catch (InvalidFatturaException e) {
            log.error("[INVALID FATTURA] {}", e.getMessage(), e);
            handleFailure(job, StatusJobErrorType.SYSTEM_ERROR, e);

        } catch (UnknownJobException e) {
            log.error("[UNKNOWN ERROR] {}", e.getMessage(), e);
            handleFailure(job, StatusJobErrorType.UNKNOWN, e);

        } finally {
            logJobEnd(startInstant, job);
        }
    }

    // HANDLER ERRORI

    // HANDLER JOB EXECUTION

    private void handleFailure(JobExecution job,
                               StatusJobErrorType type,
                               Exception e) {

        if (job == null) {
            log.error("[FATTURE JOB FAILED] (no-id) type={} msg={}", type, e.getMessage(), e);
            return;
        }

        log.error("[FATTURE JOB FAILED] id={} type={} msg={}", job.getId(), type, e.getMessage(), e);
        jobExecutionService.failed(job, type, e);
    }

    // HANDLER PER L'ENUM Fattura

    private void handleFailure(JobExecution job,
                               SXFatturaJobexecutionErrorType type,
                               FatturaWorkExecutionException e) {

        if (job == null) {
            log.error("[FATTURE JOB FAILED] (no-id) type={} msg={}", type, e.getMessage(), e);
            return;
        }

        // LOG
        log.error("[FATTURE JOB FAILED] id={} type={} msg={}",
                job.getId(), type, e.getMessage(), e);


        // La conversione  Status è RIGUARDA SOLO  questo scheduler

        StatusJobErrorType mappedForJob = mapToJobError(type);
        jobExecutionService.failed(job, mappedForJob, e);

    }

    // Mapper INERENTE SOLO ALLO SCHEDULER

    private StatusJobErrorType mapToJobError(SXFatturaJobexecutionErrorType type) {
        switch (type) {
            case BUSINESS_WARNING:
                return StatusJobErrorType.BUSINESS_WARNING;
            case BUSINESS_ERROR:
                return StatusJobErrorType.BUSINESS_ERROR;
            case SYSTEM_ERROR:
                return StatusJobErrorType.SYSTEM_ERROR;
            default:
                return StatusJobErrorType.UNKNOWN;
        }
    }

    // LOG FINE JOB

    private void logJobEnd(Instant startInstant, JobExecution job) {
        Instant endInstant = Instant.now();
        long ms = Duration.between(startInstant, endInstant).toMillis();
        log.info("--- FATTURE JOB END ---");
        log.info("[END TIME] {}", TS.format(endInstant));
        log.info("[JOB ID] {}", (job != null ? job.getId() : "null"));
        log.info("[DURATION] {}ms", ms);
    }
}