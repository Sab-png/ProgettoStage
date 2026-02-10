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

    @Scheduled(fixedDelayString = "${inventory.check.rate}")
    public void runCheck() {

        log.info("JOB INVENTORY | Avvio controllo stock");

        // FASE 1  : CREA UNA JOB_EXECUTION NEL DB (status = RUNNING)
        JobExecution job = jobExecutionService.start();

        try {
            // FASE 2: LOGICA BUSINESS DEL JOB
            magazzinoService.checkStockLevels();

            // Identificare se un job è già in RUNNING
            if (jobExecutionService.findRunning().isPresent()) {
                log.warn("JOB INVENTORY | Job già in esecuzione, skip");
                return;
            }

            // FASE 3 :  FORZAMENTO E  FALLIMENTO PER TESTING  <<<
           // if (true) {
                //throw new RuntimeException("Errore di test (force FAIL)");
           // }

            // FASE 4 : SUCCESSO DEL JOB
            jobExecutionService.success(job);
            log.info("JOB INVENTORY | Completato con SUCCESS");

        } catch (Exception e) {

            // FASE 5:  ERRORE : AVVIENE IL MAPPING AUTOMATICO A SJobErrorType
            jobExecutionService.failed(job, e);
            log.error("JOB INVENTORY | Errore durante l'esecuzione", e);

        }

        log.info("JOB INVENTORY | Fine controllo stock");
    }
}