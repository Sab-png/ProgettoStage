package it.spindox.stagelab.magazzino.sjobs;
import it.spindox.stagelab.magazzino.entities.JobExecution;
import it.spindox.stagelab.magazzino.entities.StatusJobErrorType;
import it.spindox.stagelab.magazzino.exceptions.jobsexceptions.InvalidCapacityException;
import it.spindox.stagelab.magazzino.exceptions.jobsexceptions.InvalidFatturaException;
import it.spindox.stagelab.magazzino.exceptions.jobsexceptions.UnknownJobException;
import it.spindox.stagelab.magazzino.services.JobExecutionService;
import it.spindox.stagelab.magazzino.services.MagazzinoService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import java.time.*;
import java.time.format.DateTimeFormatter;




@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor

public class InventoryScheduler {


    //  FUSO ORARIO

    private static final ZoneId ZONE_ROME = ZoneId.of("Europe/Rome");
    private static final DateTimeFormatter TS =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withZone(ZONE_ROME);


    //  DIPENDENZE

    private final MagazzinoService magazzinoService;
    private final JobExecutionService jobExecutionService;


    //     CONFIGURAZIONE

    @Value("${inventory.check.rate}")
    private long checkRateMs;

    @Value("${jobs.enabled:true}")
    private boolean jobsEnabled;



    //  1) ENTRY POINT SCHEDULER - intervallo: inventory.check.rate

    @Scheduled(fixedRateString = "${inventory.check.rate}")

    public void runCheck() {

        // 2) TIMESTAMP DI INIZIO (UTC → Europe/Rome)

        final Instant startInstant = Instant.now();
        final String startTs = TS.format(startInstant);

        log.info("--- INVENTORY JOB START --- now={} fixedRate={}ms",
                startTs, checkRateMs);

        JobExecution job = null;

        try {


            // 3) CREAZIONE RECORD JOB (stato = RUNNING)

            job = jobExecutionService.start();
            log.info("[JOB IS RUNNING] id={} startTime={}", job.getId(), job.getStartTime());


            // 4) LOGICA DEL JOB:
            //     - calcolo livelli stock
            //     - possibili eccezioni MagazzinoException

            magazzinoService.checkStockLevels();


            // 5) JOB SUCCESS

            jobExecutionService.success(job);
            log.info("[THE JOB IS SUCCEEDED] id={}", job.getId());
        }

// 6) GESTIONE ECCEZIONI

// 6.1) Errori di capacità / quantità (job)

        catch (InvalidCapacityException e) {
            log.error("[INVALID CAPACITY] {}", e.getMessage(), e);
            handleFailure(job, StatusJobErrorType.SYSTEM_ERROR, e);
        }

// 6.2) Errori fattura durante il job (date, stato, importi)

        catch (InvalidFatturaException e) {
            log.error("[INVALID FATTURA] {}", e.getMessage(), e);
            handleFailure(job, StatusJobErrorType.SYSTEM_ERROR, e);
        }


// 6.3) Fallback generico

        catch (Exception e) {
            log.error("[UNKNOWN ERROR] {}", e.getMessage(), e);
            handleFailure(job, StatusJobErrorType.UNKNOWN, e);
        }
        // 7) TIMESTAMP DI FINE + DURATA COMPLETA DEL JOB

        finally {
            logJobEnd(startInstant, job);
        }
    }


    //   LOG DELLA FINE JOB

    private void logJobEnd(Instant startInstant, JobExecution job) {
        Instant endInstant = Instant.now();
        long ms = Duration.between(startInstant, endInstant).toMillis();

        log.info("--- INVENTORY JOB ENDS---");
        log.info("[END TIMESTAMP] {}", TS.format(endInstant));
        log.info("[JOB ID] {}", (job != null ? job.getId() : "null"));
        log.info("[JOB DURATION] {}ms ({})", ms, humanReadableDuration(ms));
    }


    //      FORMATO DURATA (H, MIN, SEC)

    private String humanReadableDuration(long ms) {
        long sec = ms / 1000;
        long min = sec / 60;
        long h = min / 60;

        sec %= 60;
        min %= 60;

        return String.format("%dh %dm %ds", h, min, sec);
    }


    //      HANDLER DI FAILURE (centralizzato)

    private void handleFailure(JobExecution job,
                               StatusJobErrorType type,
                               Exception e) {

// Caso in cui il RUNNING non è stato creato

        if (job == null) {


            log.error("[FAILED] (no-id) type={} msg={}", type, e.getMessage(), e);
            return;
        }

        log.error("[FAILED] id={} type={} msg={}", job.getId(), type, e.getMessage(), e);

        jobExecutionService.failed(job, type, e);
    }
}