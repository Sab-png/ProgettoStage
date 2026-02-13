package it.spindox.stagelab.magazzino.exceptions.jobsexceptions;
import it.spindox.stagelab.magazzino.entities.StatusJobErrorType;
import org.springframework.http.HttpStatus;

public class ConfigurationException extends JobException {
    public ConfigurationException(String message) {
        super(message, StatusJobErrorType.CONFIGURATION_ERROR, HttpStatus.BAD_REQUEST);
    }
}