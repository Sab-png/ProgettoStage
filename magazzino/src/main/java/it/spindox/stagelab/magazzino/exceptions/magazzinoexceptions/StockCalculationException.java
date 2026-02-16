package it.spindox.stagelab.magazzino.exceptions.magazzinoexceptions;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class StockCalculationException extends MagazzinoException {

    public StockCalculationException(String message, Throwable cause) {
        super(message, StatusJob.FAILED, cause);

        log.error("[StockCalculationException] {}", message, cause);
    }
}