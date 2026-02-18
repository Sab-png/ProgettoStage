package it.spindox.stagelab.magazzino.exceptions.jobsexceptions;
import it.spindox.stagelab.magazzino.entities.StatusJobErrorType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Slf4j
@Getter

public class SystemException extends JobException {
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemException.class);

    public SystemException(String message) {
        super(message, StatusJobErrorType.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        LOGGER.error("[THIS IS A SYSTEM_ERROR] {}", message);
    }
}
