package it.spindox.stagelab.magazzino.entities;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum SXFatturaJobexecutionErrorType {

    SYSTEM_ERROR("Errore di sistema imprevisto", HttpStatus.INTERNAL_SERVER_ERROR),
    BUSINESS_ERROR("Errore di business", HttpStatus.BAD_REQUEST),
    BUSINESS_WARNING("Anomalia di business non bloccante", HttpStatus.OK),
    UNKNOWN("Errore sconosciuto", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String defaultMessage;
    private final HttpStatus defaultHttpStatus;

    SXFatturaJobexecutionErrorType(String defaultMessage, HttpStatus defaultHttpStatus) {
        this.defaultMessage = defaultMessage;
        this.defaultHttpStatus = defaultHttpStatus;
    }
}