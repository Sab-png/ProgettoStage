package it.spindox.stagelab.magazzino.Sjobs;
import it.spindox.stagelab.magazzino.entities.JobExecution;
import it.spindox.stagelab.magazzino.entities.SJobErrorType;
import it.spindox.stagelab.magazzino.services.JobExecutionService;
import it.spindox.stagelab.magazzino.services.MagazzinoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.*;
import jakarta.validation.ValidationException;
import org.springframework.dao.DataAccessException;
import org.springframework.web.client.RestClientException;


@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryScheduler {

    private final MagazzinoService magazzinoService;
    private final JobExecutionService jobExecutionService;

    @Scheduled(fixedDelayString = "${inventory.check.rate}")
    public void runCheck() {

        log.info("JOB INVENTORY | Avvio controllo stock");

        if (jobExecutionService.findRunning().isPresent()) {
            log.warn("JOB INVENTORY | Job già in esecuzione, skip");
            return;
        }

        // 1️ CREAZIONE JOB_EXECUTION (RUNNING)
        JobExecution job = jobExecutionService.start();
        long start = System.currentTimeMillis();
        LocalDateTime startAt = LocalDateTime.now();   //  LOG TIME INIZIO

        log.info("JOB INVENTORY | START | jobId={} | at={}", job.getId(), startAt);

        try {
            // 2️ LOGICA DEL JOB
            magazzinoService.checkStockLevels();

            // 3️ SUCCESS
            jobExecutionService.success(job);

            LocalDateTime endAt = LocalDateTime.now();   //  LOG TIME FINE
            log.info(
                    "JOB INVENTORY | END SUCCESS | jobId={} | at={} | end={} | duration={}ms",
                    job.getId(),
                    startAt,
                    endAt,
                    System.currentTimeMillis() - start
            );

        } catch (Exception e) {

            // 4️ FALLIMENTO – MAPPATURA TIPIZZATA
            SJobErrorType errorType = mapErrorType(e);

            LocalDateTime endAt = LocalDateTime.now();  //  LOG TIME FINE
            jobExecutionService.failed(job, errorType, e);

            log.error(
                    "JOB INVENTORY | END FAILED | jobId={} | at={} | end={} | duration={}ms | errorType={} | msg={}",
                    job.getId(),
                    startAt,
                    endAt,
                    System.currentTimeMillis() - start,
                    errorType,
                    e.getMessage(),
                    e
            );
        }

        log.info("JOB INVENTORY | Fine controllo stock");
    }

    // mapErrorType rimane identico
    private SJobErrorType mapErrorType(Exception e) {
        if (e instanceof ValidationException || e instanceof IllegalArgumentException)
            return SJobErrorType.VALIDATION_ERROR;

        if (e instanceof RestClientException
                || e instanceof java.net.SocketTimeoutException
                || e instanceof java.net.ConnectException)
            return SJobErrorType.EXTERNAL_SERVICE;

        if (e instanceof DataAccessException
                || e instanceof java.sql.SQLException
                || e instanceof java.io.IOException
                || e instanceof java.util.concurrent.TimeoutException)
            return SJobErrorType.TECHNICAL_ERROR;

        if (e instanceof SecurityException
                || e instanceof javax.security.auth.login.LoginException)
            return SJobErrorType.SECURITY_ERROR;

        if (e instanceof java.util.MissingResourceException
                || e instanceof IllegalStateException)
            return SJobErrorType.CONFIGURATION_ERROR;

        return SJobErrorType.UNKNOWN;
    }
}


























