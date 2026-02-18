package it.spindox.stagelab.magazzino.exceptions.prodottoexceptions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
public class InvalidQuantityException extends ProdottoException {

    public InvalidQuantityException(Long prodottoId, Integer quantita, String message) {
        super();
        log.error("[InvalidQuantityException] prodottoId={}, quantita={}", prodottoId, quantita);
    }

    public InvalidQuantityException(String dbValue, String message) {
        super();
    }
}