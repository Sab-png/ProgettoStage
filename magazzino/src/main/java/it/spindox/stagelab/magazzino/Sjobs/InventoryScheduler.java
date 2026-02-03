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

@Component
@EnableScheduling
public class InventoryScheduler {

    private static final Logger logger =
            Logger.getLogger(InventoryScheduler.class.getName());

    private final MagazzinoService magazzinoService;
    private final JobExecutionService jobExecutionService;

    public InventoryScheduler(MagazzinoService magazzinoService,
                              JobExecutionService jobExecutionService) {
        this.magazzinoService = magazzinoService;
        this.jobExecutionService = jobExecutionService;
    }

    @Scheduled(fixedRateString = "${inventory.check.rate}")
    public void runCheck() {

        logger.info("Il job sta iniziando");

        // Avvio
        JobExecution job = jobExecutionService.start();

        try {
            magazzinoService.checkStockLevels();
            jobExecutionService.success(job);

            // Log di fine in caso di successo
            logger.info("Il job è finito (SUCCESS)");

        } catch (Exception e) {
            jobExecutionService.error(job, e);

            logger.log(Level.SEVERE, "Errore durante il job", e);

            // Log di fine in caso di errore
            logger.info("Il job è finito (ERROR)");

            throw e; // segnala a Spring che il task è fallito
        }
    }
}