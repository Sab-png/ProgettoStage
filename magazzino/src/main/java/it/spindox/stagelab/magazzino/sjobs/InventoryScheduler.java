package it.spindox.stagelab.magazzino.sjobs;
import it.spindox.stagelab.magazzino.entities.JobExecution;
import it.spindox.stagelab.magazzino.entities.StatusJobErrorType;
import it.spindox.stagelab.magazzino.exceptions.jobsexceptions.SystemException;
import it.spindox.stagelab.magazzino.exceptions.jobsexceptions.TechnicalException;
import it.spindox.stagelab.magazzino.exceptions.magazzinoexceptions.MagazzinoException;
import it.spindox.stagelab.magazzino.services.JobExecutionService;
import it.spindox.stagelab.magazzino.services.MagazzinoService;
import jakarta.validation.ValidationException;
import org.hibernate.internal.util.config.ConfigurationException;
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

    // FUSO ORARIO (Europe/Rome)

    private static final ZoneId ZONE_ROME = ZoneId.of("Europe/Rome");
    private static final DateTimeFormatter TS =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withZone(ZONE_ROME);


    //  Dipendenze

    private final MagazzinoService magazzinoService;
    private final JobExecutionService jobExecutionService;


    //  Configurazione esterna (application.properties)

    @Value("${inventory.check.rate}")
    private long checkRateMs;               // intervallo tra due run consecutive

    @Value("${jobs.enabled:true}")
    private boolean jobsEnabled;            // mezzo per abilitare/disabilitare l’esecuzione dei job


    // 1) Entry-point dello Scheduler
    //    Esegue  : inventory.check.rate

    @Scheduled(fixedRateString = "${inventory.check.rate}")
    public void runCheck() {


        //  Validazione della configurazione PRIMA di avviare il job
        //  se viene invalidato :  si passa dal ConfigurationException e poi si esce dal flusso

        if (!jobsEnabled) {
            throw new ConfigurationException("I job sono disabilitati: jobs.enabled=false");
        }
        if (checkRateMs <= 0) {
            throw new ConfigurationException("Configurazione invalida: inventory.check.rate deve essere > 0");
        }


        // 2) Timestamp di INIZIO (UTC + formattato Europe/Rome)


        final Instant startInstant = Instant.now();
        final String startTs = TS.format(startInstant);

        log.info("=== INVENTORY JOB START === now={} fixedRate={}ms", startTs, checkRateMs);

        JobExecution job = null;

        try {

            // 3) Creazione record Job in stato RUNNING


            job = jobExecutionService.start();
            log.info("[JOB IS RUNNING] Job id={} startTime={}", job.getId(), job.getStartTime());


            // 4) LOGICA DEL JOB
            //     - calcolo livelli di stock per tutti i magazzini
            //     - lancia MagazzinoException in caso di dati invalidi

            magazzinoService.checkStockLevels();


            // [5] IF THE JOB IS SUCCESS + endTime

            jobExecutionService.success(job);
            log.info("[THE JOB IS SUCCESSED] Job id={}", job.getId());
        }


        // [6] GESTIONE ECCEZIONI :
        //     -  salva FAILED con errorType coerentI


        // 6.1) Configurazione applicativa/mancante/errata

        catch (ConfigurationException e) {
            log.error("[CONFIG ERROR] {}", e.getMessage(), e);
            handle(job, StatusJobErrorType.CONFIGURATION_ERROR, e);
        }

        // 6.2) Errori di dominio Magazzino (capacità invalida, quantità negative, calcolo)

        catch (MagazzinoException e) {
            log.error("[MAGAZZINO ERROR] {}", e.getMessage(), e);
            handle(job, StatusJobErrorType.VALIDATION_ERROR, e);
        }

        // 6.3) Errori di validazione generici (parametri job/DTO)

        catch (ValidationException e) {
            log.error("[VALIDATION ERROR] {}", e.getMessage(), e);
            handle(job, StatusJobErrorType.VALIDATION_ERROR, e);
        }

        // 6.4) Errori tecnici/transitori (timeout, DB non disponibile, rete)

        catch (TechnicalException e) {
            log.error("[TECHNICAL ERROR] {}", e.getMessage(), e);
            handle(job, StatusJobErrorType.TECHNICAL_ERROR, e);
        }

        // 6.5) Errori di sistema non transitori (bug interni, violazioni invarianti non di input)

        catch (SystemException e) {
            log.error("[SYSTEM ERROR] {}", e.getMessage(), e);
            handle(job, StatusJobErrorType.SYSTEM_ERROR, e);
        }

        // 6.6) Fallback: tutto ciò che non rientra nei casi noti

        catch (Exception e) {
            log.error("[UNKNOWN ERROR] {}", e.getMessage(), e);
            handle(job, StatusJobErrorType.UNKNOWN, e);
        }


// 7) TIME FINE JOB CON RELATIVI DATI: H, MIN, SEC e timestamp finale

        finally {
            Instant endInstant = Instant.now();
            long ms = Duration.between(startInstant, endInstant).toMillis();
            String endTs = TS.format(endInstant); // Formattato Europe/Rome

            log.info("=== INVENTORY JOB END ===");
            log.info("[END TIMESTAMP] {}", endTs);
            log.info("[JOB ID] {}", job != null ? job.getId() : "null");
            log.info("[JOB DURATION] {}ms ({})", ms, humanReadableDuration(ms));

        }
    }


    // Durata in formato : ore, min,sec

    private String humanReadableDuration(long ms) {
        long sec = ms / 1000;
        long min = sec / 60;
        long h = min / 60;
        sec %= 60;
        min %= 60;
        return String.format("%dh %dm %ds", h, min, sec);
    }


    // Gestione della FAILURE:
    // - normalizzazione Throwable :  Exception
    // - salvataggio FAILED con errorType e messaggio


    private void handle(JobExecution job, StatusJobErrorType type, Throwable e) {
        if (job == null) {
            // Non è stato possibile creare il record RUNNING: logghiamo comunque
            log.error("[FAILED] (no-id) type={} msg={}", type, e.getMessage(), e);
            return;
        }

        log.error("[FAILED] id={} type={} msg={}", job.getId(), type, e.getMessage(), e);

        Exception ex = (e instanceof Exception)
                ? (Exception) e
                : new Exception(e); // wrapping per uniformare la firma del service

        jobExecutionService.failed(job, type, ex);
    }
}