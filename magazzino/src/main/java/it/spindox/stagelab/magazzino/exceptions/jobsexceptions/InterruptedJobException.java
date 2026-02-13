package it.spindox.stagelab.magazzino.exceptions.jobsexceptions;
import it.spindox.stagelab.magazzino.entities.StatusJobErrorType;
import org.springframework.http.HttpStatus;

public class InterruptedJobException extends JobException {
    public InterruptedJobException(String message) {
        super(message, StatusJobErrorType.INTERRUPTED, HttpStatus.CONFLICT);
    }
}