package it.spindox.stagelab.magazzino.exceptions.jobsexceptions;
import it.spindox.stagelab.magazzino.entities.StatusJobErrorType;
import org.springframework.http.HttpStatus;

public class TechnicalException extends JobException {
    public TechnicalException(String message) {
        super(message, StatusJobErrorType.TECHNICAL_ERROR, HttpStatus.SERVICE_UNAVAILABLE);
    }
}