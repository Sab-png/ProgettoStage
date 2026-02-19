package it.spindox.stagelab.magazzino.entities;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter

public enum StatusJobErrorType {

    SYSTEM_ERROR("Errore di sistema imprevisto", HttpStatus.INTERNAL_SERVER_ERROR),
    UNKNOWN("Errore sconosciuto", HttpStatus.INTERNAL_SERVER_ERROR);

// Messaggio di stringa di default

    private final String defaultMessage;

    //Status HTTP per questa categoria di errore

    private final HttpStatus defaultHttpStatus;

    // torna un  messaggio di default per la  tipologia di errore
    StatusJobErrorType(String defaultMessage, HttpStatus defaultHttpStatus) {
        this.defaultMessage = defaultMessage;
        this.defaultHttpStatus = defaultHttpStatus;
    }




}