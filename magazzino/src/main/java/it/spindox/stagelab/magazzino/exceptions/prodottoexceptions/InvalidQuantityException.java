package it.spindox.stagelab.magazzino.exceptions.prodottoexceptions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
public class InvalidQuantityException extends ProdottoException {

    private final Long prodottoId;
    private final Integer quantita;
    private final String dbValue;   // puo' essere facoltativo

    public InvalidQuantityException(Long prodottoId, Integer quantita, String message) {
        super(message);
        this.prodottoId = prodottoId;
        this.quantita = quantita;
        this.dbValue = null;

        log.error("[InvalidQuantityException] prodottoId={}, quantita={}, msg={}",
                prodottoId, quantita, message);
    }

    public InvalidQuantityException(String dbValue, String message) {
        super(message);
        this.prodottoId = null;
        this.quantita = null;
        this.dbValue = dbValue;

        log.error("[InvalidQuantityException] dbValue={}, msg={}", dbValue, message);
    }
}