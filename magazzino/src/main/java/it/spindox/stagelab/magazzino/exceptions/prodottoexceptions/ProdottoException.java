package it.spindox.stagelab.magazzino.exceptions.prodottoexceptions;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@Getter
public class ProdottoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private StatusJob errorType;

    public ProdottoException(String message) {
        super(message);
    }

    public ProdottoException(String message, StatusJob errorType, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }

    public StatusJob getErrorType() {
        return errorType;
    }
}