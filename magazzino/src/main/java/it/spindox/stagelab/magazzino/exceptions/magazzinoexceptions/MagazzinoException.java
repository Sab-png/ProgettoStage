package it.spindox.stagelab.magazzino.exceptions.magazzinoexceptions;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import lombok.Getter;


@Getter

public class MagazzinoException extends RuntimeException {
    private final StatusJob errorType;

    public MagazzinoException(String message, StatusJob errorType) {
        super(message);
        this.errorType = errorType;
    }

    public MagazzinoException(String message, StatusJob errorType, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }
}