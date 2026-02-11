package it.spindox.stagelab.magazzino.Sjobs;
import it.spindox.stagelab.magazzino.entities.JobExecution;
import it.spindox.stagelab.magazzino.entities.SJobErrorType;
import it.spindox.stagelab.magazzino.services.JobExecutionService;
import it.spindox.stagelab.magazzino.services.MagazzinoService;
import org.hibernate.internal.util.config.ConfigurationException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Optional;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import java.time.*;
import java.time.format.DateTimeFormatter;



@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class InventoryScheduler {

    /**
     * Timezone applicativa utilizzata nei log
     */
    private static final ZoneId ZONE_ROME = ZoneId.of("Europe/Rome");

    /**
     * Format leggibile per timestamp
     */
    private static final DateTimeFormatter TS =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withZone(ZONE_ROME);

    private final MagazzinoService magazzinoService;
    private final JobExecutionService jobExecutionService;

    /**
     * Intervallo tra una run e la successiva, letto da application.properties
     */
    @Value("${inventory.check.rate}")
    private long checkRateMs;

    /**
     * Metodo schedulato che esegue il controllo inventario a intervalli regolari.
     * Il valore è configurato tramite la proprietà "inventory.check.rate".
     */
    @Scheduled(fixedRateString = "${inventory.check.rate}")
    public void runCheck() {

        // =============================
        // 1) Timestamp di avvio job
        // =============================

        final Instant startInstant = Instant.now();                     // Tempo UTC
        final String startTs = TS.format(startInstant);                 // Tempo formattato in Europe/Rome

        log.info("[InventoryScheduler] Avvio scheduler: Inizio Work — now={} (Europe/Rome), fixedRate={} ms",
                startTs, checkRateMs);


        // =============================
        // 2) Prevenzione esecuzioni concorrenti
        // =============================

        Optional<JobExecution> running = jobExecutionService.findRunning();
        if (running.isPresent()) {
            log.warn("Job già in esecuzione — runningId={}, startTime={}",
                    running.get().getId(), running.get().getStartTime());
            return;
        }


        JobExecution job = null;

        try {

            // =============================
            // 3) Registrazione job in RUNNING
            // =============================

            job = jobExecutionService.start();

            // Aggiunge jobId al MDC per arricchire automaticamente i log
            MDC.put("jobId", String.valueOf(job.getId()));

            log.info("JOB START — id={}, dbStartTime={}, appStartTime={}",
                    job.getId(),
                    job.getStartTime().atZoneSameInstant(ZoneId.of("Europe/Rome")).toLocalDateTime(),
                    startTs);

            // =============================
            // 4) Logica applicativa principale
            // =============================

            magazzinoService.checkStockLevels();


            // =============================
            // 5) Successo del job
            // =============================

            jobExecutionService.success(job);
            log.info("JOB SUCCESS — id={}", job.getId());


        } catch (IllegalArgumentException e) {

            handle(job, SJobErrorType.VALIDATION_ERROR, e);

        } catch (ConfigurationException e) {

            handle(job, SJobErrorType.CONFIGURATION_ERROR, e);


        } catch (SecurityException e) {

            handle(job, SJobErrorType.SECURITY_ERROR, e);

        } catch (RuntimeException e) {

            handle(job, SJobErrorType.TECHNICAL_ERROR, e);

        } catch (Exception e) {

            handle(job, SJobErrorType.UNKNOWN, e);

        } finally {

            // =============================
            // 6) Log di chiusura con durata
            // =============================

            final Instant endInstant = Instant.now();
            final String endTs = TS.format(endInstant);

            long durationMs = Duration.between(startInstant, endInstant).toMillis();
            String humanDuration = humanReadableDuration(durationMs);

            Long jobId = (job != null ? job.getId() : null);

            log.info("[InventoryScheduler] Fine scheduler: job Startato con successo — id={}, start={}, end={}, durata={} ms ({})",
                    jobId, startTs, endTs, durationMs, humanDuration);

            // Rimuove jobId dal MDC
            MDC.remove("jobId");
        }
    }

    /**
     * Converte la durata in un formato leggibile (implementazione vuota come da tuo codice)
     */
    private String humanReadableDuration(long durationMs) {
        return "";
    }

    /**
     * Metodo centralizzato per gestire errori di job.
     * Accetta Throwable per evitare problemi con eccezioni non-Exception.
     */
    private void handle(JobExecution job, SJobErrorType type, Throwable e) {

        if (job == null) {
            log.error("JOB FAILED — (no-id) type={}, msg={}", type, e.getMessage(), e);
            return;
        }

        log.error("JOB FAILED — id={}, type={}, msg={}",
                job.getId(), type, e.getMessage(), e);

        // Il service accetta Exception → se è Throwable lo impacchettiamo
        Exception ex = (e instanceof Exception)
                ? (Exception) e
                : new Exception(e);

        jobExecutionService.failed(job, type, ex);
    }
}
