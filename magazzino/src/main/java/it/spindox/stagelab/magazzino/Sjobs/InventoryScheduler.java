package it.spindox.stagelab.magazzino.Sjobs;
import it.spindox.stagelab.magazzino.entities.JobExecution;
import it.spindox.stagelab.magazzino.services.JobExecutionService;
import it.spindox.stagelab.magazzino.services.MagazzinoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
@Component

public class InventoryScheduler {



    private final MagazzinoService magazzinoService;
    private final JobExecutionService jobExecutionService;

    public InventoryScheduler(MagazzinoService magazzinoService,
                              JobExecutionService jobExecutionService) {
        this.magazzinoService = magazzinoService;
        this.jobExecutionService = jobExecutionService;
    }

    @Scheduled(fixedRateString = "${inventory.check.rate}")
    public void runCheck() {

        log.info("Il job sta iniziando");

        // Avvio
        JobExecution job = jobExecutionService.start();

        try {
            magazzinoService.checkStockLevels();
            jobExecutionService.success(job);

            // Log di fine in caso di successo
            log.info("Il job è finito (SUCCESS)");

        } catch (Exception e) {
            jobExecutionService.error(job, e);

            log.info( "Errore durante il job", e);

            // Log di fine in caso di errore
            log.info("Il job è finito (ERROR)");

            throw e; // segnala a Spring che il task è fallito
        }
    }
}