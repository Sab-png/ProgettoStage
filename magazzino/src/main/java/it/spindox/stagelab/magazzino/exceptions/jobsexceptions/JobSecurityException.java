package it.spindox.stagelab.magazzino.exceptions.jobsexceptions;
import it.spindox.stagelab.magazzino.entities.StatusJobErrorType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Slf4j
@Getter
public class JobSecurityException extends JobException {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobSecurityException.class);

    public JobSecurityException(String message) {
        super(message, StatusJobErrorType.SECURITY_ERROR, HttpStatus.FORBIDDEN);
        LOGGER.warn("[THIS IS A SECURITY_ERROR] {}", message);
    }
}
