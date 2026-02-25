package it.spindox.stagelab.magazzino.exceptions.jobsexceptions;
import it.spindox.stagelab.magazzino.entities.StatusJobErrorType;
import lombok.Getter;
import org.springframework.http.HttpStatus;



// Base class per tutte le eccezioni dei Job
// RuntimeException: non obbliga a dichiararle
// Contiene semantica di errore di job (errorType) e HTTP mapping (httpStatus)


@Getter
public abstract class JobException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    // Tipo di errore del job (es. SYSTEM_ERROR, UNKNOWN)

    private final StatusJobErrorType errorType;

    // HTTP status che il GlobalExceptionHandler restituirà al client
    private final HttpStatus httpStatus;


    // Costruttore principale: TUTTE le sottoclassi devono chiamare questo.

    //  @param message    messaggio dell'errore
    //  @param errorType  tipo di errore del job
    // @param httpStatus HTTP status associato all'errore

    protected JobException(String message,
                           StatusJobErrorType errorType,
                           HttpStatus httpStatus) {
        super(message);
        this.errorType = errorType;
        this.httpStatus = httpStatus;
    }
}
