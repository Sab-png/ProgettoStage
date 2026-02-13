package it.spindox.stagelab.magazzino.exceptions.jobsexceptions;
import it.spindox.stagelab.magazzino.entities.StatusJobErrorType;
import org.springframework.http.HttpStatus;

public class ValidationException extends JobException {
    public ValidationException(String message) {
        super(message, StatusJobErrorType.VALIDATION_ERROR, HttpStatus.BAD_REQUEST);
    }
}