package it.spindox.stagelab.magazzino.exceptions.jobsexceptions;
import it.spindox.stagelab.magazzino.entities.StatusJobErrorType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Slf4j
@Getter
public class TechnicalException extends JobException {
    private static final Logger LOGGER = LoggerFactory.getLogger(TechnicalException.class);

    public TechnicalException(String message) {
        super(message, StatusJobErrorType.TECHNICAL_ERROR, HttpStatus.SERVICE_UNAVAILABLE);
        LOGGER.error("[THIS IS A TECHNICAL_ERROR] {}", message);
    }
}
