package it.spindox.stagelab.magazzino.sjobs;
import it.spindox.stagelab.magazzino.entities.JobExecution;
import it.spindox.stagelab.magazzino.entities.StatusJobErrorType;
import it.spindox.stagelab.magazzino.exceptions.jobsexceptions.InvalidFatturaException;
import it.spindox.stagelab.magazzino.services.FatturaService;
import it.spindox.stagelab.magazzino.services.JobExecutionService;
import jakarta.annotation.PostConstruct;
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

    private final FatturaService fatturaService;
    private final JobExecutionService jobExecutionService;


    // application.properties

    @Value("${fatture.check.enabled:true}")
    private boolean fattureJobEnabled;

    @Value("${fatture.check.cron:0 0 1 * * *}")
    private String fattureCheckCron;

    // TIMEZONE

    private static final ZoneId ZONE_ROME = ZoneId.of("Europe/Rome");

    private static final DateTimeFormatter TS =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                    .withZone(ZONE_ROME);



    // FATTURA SCHEDULER  GIORNALIERO: CONTROLLO STATO FATTURE

    @Scheduled(cron = "${fatture.check.cron}", zone = "Europe/Rome")
    public void runFattureDailyCheck() {

        if (!fattureJobEnabled) {
            log.info("[FATTURE JOB] DISABILITATO via config (fatture.check.enabled=false).");
            return;
        }

        if (fattureCheckCron == null || fattureCheckCron.isBlank()) {
            log.warn("[FATTURE JOB] CRON NON CONFIGURATO correttamente (fatture.check.cron).");
            return;
        }

        final Instant startInstant = Instant.now();
        log.info("--- FATTURE JOB START --- [{}] ---", TS.format(startInstant));

        JobExecution job = null;

        try {
            // 1) JOB RUNNING

            job = jobExecutionService.start();
            log.info("[JOB RUNNING] id={}", job.getId());

            // 2) LOGICA DEL JOB

            fatturaService.paymentCheckAllFatture();

            // 3) SUCCESS

            jobExecutionService.success(job);
            log.info("[FATTURE JOB SUCCESS] id={}", job.getId());

        } catch (InvalidFatturaException e) {
            log.error("[INVALID FATTURA] {}", e.getMessage(), e);
            handleFailure(job, StatusJobErrorType.SYSTEM_ERROR, e);

        } catch (Exception e) {
            log.error("[UNKNOWN ERROR] {}", e.getMessage(), e);
            handleFailure(job, StatusJobErrorType.UNKNOWN, e);

        } finally {
            logJobEnd(startInstant, job);
        }
    }

    // HANDLER DI FALLIMENTO

    private void handleFailure(JobExecution job,
                               StatusJobErrorType type,
                               Exception e) {

        if (job == null) {
            log.error("[FATTURE JOB FAILED] (no-id) type={} msg={}", type, e.getMessage());
            return;
        }

        log.error("[FATTURE JOB FAILED] id={} type={} msg={}", job.getId(), type, e.getMessage());
        jobExecutionService.failed(job, type, e);
    }

    // LOG DI FINE JOB

    private void logJobEnd(Instant startInstant, JobExecution job) {

        Instant endInstant = Instant.now();
        long ms = Duration.between(startInstant, endInstant).toMillis();

        log.info("--- FATTURE JOB END ---");
        log.info("[END TIME] {}", TS.format(endInstant));
        log.info("[JOB ID] {}", (job != null ? job.getId() : "null"));
        log.info("[DURATION] {}ms", ms);
    }
    @PostConstruct
    public void loaded() {
        log.info(">>> FATTURA SCHEDULER LOADED <<< enabled={} cron={}", fattureJobEnabled, fattureCheckCron);
    }
}
