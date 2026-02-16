package it.spindox.stagelab.magazzino.entities;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum StatusJobErrorType {

    VALIDATION_ERROR("Dati non validi", HttpStatus.BAD_REQUEST),
    CONFIGURATION_ERROR("Configurazione non valida o mancante", HttpStatus.BAD_REQUEST),
    TECHNICAL_ERROR("Errore tecnico: risorsa non disponibile o timeout", HttpStatus.SERVICE_UNAVAILABLE),
    SECURITY_ERROR("Accesso non autorizzato o permessi insufficienti", HttpStatus.FORBIDDEN),
    SYSTEM_ERROR("Errore di sistema imprevisto", HttpStatus.INTERNAL_SERVER_ERROR),
    INTERRUPTED("Job interrotto", HttpStatus.CONFLICT),
    UNKNOWN("Errore sconosciuto", HttpStatus.INTERNAL_SERVER_ERROR);

// Messaggio di stringa di default

    private final String defaultMessage;

    //Status HTTP per questa categoria di errore

    private final HttpStatus defaultHttpStatus;

    StatusJobErrorType(String defaultMessage, HttpStatus defaultHttpStatus) {
        this.defaultMessage = defaultMessage;
        this.defaultHttpStatus = defaultHttpStatus;
    }

    // torna un  messaggio di default per la  tipologia di errore

    // torna un status HTTP per tipologia di errore

}