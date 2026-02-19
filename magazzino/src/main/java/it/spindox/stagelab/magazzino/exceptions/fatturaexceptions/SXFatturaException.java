package it.spindox.stagelab.magazzino.exceptions.fatturaexceptions;
import lombok.Getter;

@Getter

public abstract class SXFatturaException extends RuntimeException {

    public SXFatturaException(String message) {
        super(message);
    }

    public SXFatturaException(String message, Throwable cause) {
        super(message, cause);
    }
}