package it.spindox.stagelab.magazzino.exceptions.jobsexceptions;
import it.spindox.stagelab.magazzino.entities.StatusJobErrorType;
import org.springframework.http.HttpStatus;

public class UnknownJobException extends JobException {
    public UnknownJobException(String message) {
        super(message, StatusJobErrorType.UNKNOWN, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}