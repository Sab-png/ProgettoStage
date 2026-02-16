package it.spindox.stagelab.magazzino.exceptions.jobsexceptions;
import it.spindox.stagelab.magazzino.entities.StatusJobErrorType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Slf4j
@Getter
public class ConfigurationException extends JobException {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationException.class);

    public ConfigurationException(String message) {
        super(message, StatusJobErrorType.CONFIGURATION_ERROR, HttpStatus.BAD_REQUEST);
        LOGGER.error("[THIS IS A CONFIGURATION_ERROR] {}", message);
    }
}
