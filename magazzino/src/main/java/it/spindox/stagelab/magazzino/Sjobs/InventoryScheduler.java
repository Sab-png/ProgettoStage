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

        //  PRIMA DI TUTTO: controllo iniziale
        if (jobExecutionService.findRunning().isPresent()) {
            log.warn("JOB INVENTORY | Job già in esecuzione, skip");
            return;
        }

        // FASE 1 CREAZIONE DI  UNA JOB_EXECUTION NEL DB (status = RUNNING)
        JobExecution job = jobExecutionService.start();

        try {
            // FASE 2 :  LOGICA BUSINESS DEL JOB
            magazzinoService.checkStockLevels();

            //  SIMULAZIONE DI ERRORE SPECIFICO
            //if (true) {
               // throw new IllegalArgumentException("Campo obbligatorio mancante");
           // }



            // FASE 3: IL JOB E' COMPLETATO CON  SUCCESSO
            jobExecutionService.success(job);
            log.info("JOB INVENTORY | Completato con SUCCESS");

        } catch (Exception e) {

            // FASE 4 :  ERRORE — MAPPING AUTOMATICO A SJobErrorType
            jobExecutionService.failed(job, e);
            log.error("JOB INVENTORY | Errore durante l'esecuzione", e);
        }

        log.info("JOB INVENTORY | Fine controllo stock");
    }
}

// l mapping degli errori avviene grazie a mapErrorType
//
// private SJobErrorType mapErrorType(Exception e)

// FASE per Testing di FORZAMENTO E  FALLIMENTO su DB per provare gli errori  <<<
// if (true) {
//throw new RuntimeException("Errore di test (force FAIL)");
// }

//
//if (true) {
       // throw new IllegalArgumentException("Campo obbligatorio mancante");

//if (true) {
//    throw new IllegalStateException("Configurazione non valida");
//}


//if (true) {
//    throw new java.net.SocketTimeoutException("Timeout di rete");
//}


//if (true) {
//    throw new org.springframework.web.client.RestClientException("Errore API esterna");
//}


//if (true) {
//    throw new org.springframework.security.access.AccessDeniedException("Accesso negato");
//}
//``

//if (true) {
//    throw new NullPointerException("Null pointer durante esecuzione");
//}




//if (true) {
//    throw new InterruptedException("Thread interrotto");
//}


//if (true) {
//    throw new Exception("Errore generico");
//













