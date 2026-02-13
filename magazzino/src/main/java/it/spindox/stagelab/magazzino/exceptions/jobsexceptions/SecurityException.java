package it.spindox.stagelab.magazzino.exceptions.jobsexceptions;
import it.spindox.stagelab.magazzino.entities.StatusJobErrorType;
import org.springframework.http.HttpStatus;

public class SecurityException extends JobException {
    public SecurityException(String message) {
        super(message, StatusJobErrorType.SECURITY_ERROR, HttpStatus.FORBIDDEN);
    }
}