package it.spindox.stagelab.magazzino.exceptions.jobsexceptions;
import it.spindox.stagelab.magazzino.entities.StatusJobErrorType;
import org.springframework.http.HttpStatus;

public class SystemException extends JobException {
    public SystemException(String message) {
        super(message, StatusJobErrorType.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}