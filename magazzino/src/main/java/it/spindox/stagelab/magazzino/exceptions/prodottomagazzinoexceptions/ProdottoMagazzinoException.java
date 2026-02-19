package it.spindox.stagelab.magazzino.exceptions.prodottomagazzinoexceptions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
public abstract class ProdottoMagazzinoException extends RuntimeException {

    protected ProdottoMagazzinoException(String message) {
        super(message);
        log.error("[PM-ERROR] {}", message);
    }

    protected ProdottoMagazzinoException(String message, Throwable cause) {
        super(message, cause);
        log.error("[PM-ERROR] {} | cause={}", message, cause.toString());
    }
}