package it.spindox.stagelab.magazzino.exceptions.jobsexceptions;
import it.spindox.stagelab.magazzino.entities.StatusJobErrorType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Slf4j
@Getter
public class InterruptedJobException extends JobException {
    private static final Logger LOGGER = LoggerFactory.getLogger(InterruptedJobException.class);

    public InterruptedJobException(String message) {
        super(message, StatusJobErrorType.INTERRUPTED, HttpStatus.CONFLICT);
        LOGGER.warn("[THIS JOB IS INTERRUPTED] {}", message);
    }
}