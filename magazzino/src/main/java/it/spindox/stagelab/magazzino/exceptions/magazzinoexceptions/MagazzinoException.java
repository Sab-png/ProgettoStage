package it.spindox.stagelab.magazzino.exceptions.magazzinoexceptions;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import lombok.Getter;
import java.io.Serial;


@Getter

public class MagazzinoException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private StatusJob errorType;

    public MagazzinoException(String message) {
        super(message);
    }

    public MagazzinoException(String message, StatusJob errorType, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }

    public MagazzinoException(String s, StatusJob statusJob) {
    }

    public StatusJob getErrorType() {
        return errorType;
    }
}