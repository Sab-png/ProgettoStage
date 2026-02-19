package it.spindox.stagelab.magazzino.exceptions.fatturaexceptions;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import lombok.Getter;


@Getter

public class SXFatturaException extends RuntimeException {

    private final StatusJob errorType;


    // Costruttore principale: messaggio + tipo errore

    public SXFatturaException(String message, StatusJob errorType) {
        super(message);
        this.errorType = errorType;
    }

    // Costruttore con causa

    public SXFatturaException(String message, StatusJob errorType, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }

    // Costruttore con tipo errore

    public SXFatturaException(StatusJob errorType) {
        super("Errore generico sulla fattura");
        this.errorType = errorType;
    }
}