package it.spindox.stagelab.magazzino.Sjobs;
import java.util.logging.Logger;
import it.spindox.stagelab.magazzino.services.MagazzinoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@EnableScheduling
public class InventoryScheduler {

    private static final Logger logger = Logger.getLogger(InventoryScheduler.class.getName());

    private final MagazzinoService magazzinoService;

    public InventoryScheduler(MagazzinoService magazzinoService) {
        this.magazzinoService = magazzinoService;
    }

    @Scheduled(fixedRateString = "${inventory.check.rate}")
    public void runCheck() {
        logger.info(" Avvio job: controllo livelli di stock");

        magazzinoService.checkStockLevels();

        logger.info(" Fine job: controllo completato");
    }
}