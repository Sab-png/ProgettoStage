package it.spindox.stagelab.magazzino.Sjobs;
import it.spindox.stagelab.magazzino.entities.JobExecution;
import it.spindox.stagelab.magazzino.services.JobExecutionService;
import it.spindox.stagelab.magazzino.services.MagazzinoService;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryScheduler {

    private final MagazzinoService magazzinoService;
    private final JobExecutionService jobExecutionService;

    /**
     * Job schedulato di controllo stock.
     * Usa fixedDelay per evitare overlap.
     */
    @Scheduled(fixedDelayString = "${inventory.check.rate}")
    public void runCheck() {

        log.info("JOB INVENTORY | Avvio controllo stock");

        //  Avvio job
        JobExecution job = jobExecutionService.start();

        try {
            //  Business logic
            magazzinoService.checkStockLevels();

            //  Successo
            jobExecutionService.success(job);
            log.info("JOB INVENTORY | Completato con SUCCESS");

        } catch (Exception e) {

            // Errore
            jobExecutionService.failed(job, e);
            log.error("JOB INVENTORY | Errore durante l'esecuzione", e);

            log.info("JOB INVENTORY | Fine controllo stock");

        }
    }
}