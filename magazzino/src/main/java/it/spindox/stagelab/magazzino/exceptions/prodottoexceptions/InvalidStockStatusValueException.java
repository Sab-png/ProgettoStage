package it.spindox.stagelab.magazzino.exceptions.prodottoexceptions;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
public class InvalidStockStatusValueException extends ProdottoException {

// ECCEZZIONE DI STOCK STATUS

    public InvalidStockStatusValueException(String dbValue, String message) {
        super(message, StatusJob.FAILED);

        log.error("[InvalidStockStatusValueException] dbValue={} msg={}", dbValue, message);
    }

    public InvalidStockStatusValueException(String dbValue, String message, Throwable cause) {
        super(message, StatusJob.FAILED, cause);

        log.error("[InvalidStockStatusValueException] dbValue={} msg={}", dbValue, message, cause);
    }
}