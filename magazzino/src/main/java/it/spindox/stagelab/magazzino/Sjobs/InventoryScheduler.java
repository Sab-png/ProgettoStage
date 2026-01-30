package it.spindox.stagelab.magazzino.Sjobs;
import it.spindox.stagelab.magazzino.services.MagazzinoService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class InventoryScheduler {

    private final MagazzinoService magazzinoService;

    public InventoryScheduler(MagazzinoService magazzinoService) {
        this.magazzinoService = magazzinoService;
    }

    @Scheduled(fixedRateString = "${inventory.check.rate}")
    public void runCheck() {
        magazzinoService.checkStockLevels();
    }
}
