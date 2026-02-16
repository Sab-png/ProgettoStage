package it.spindox.stagelab.magazzino.exceptions.prodottoexceptions;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import lombok.Getter;
import java.io.Serial;


@Getter
public class ProdottoException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ProdottoException(String message) {
        super(message);
    }

    public ProdottoException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProdottoException(String message, StatusJob statusJob) {
    }

    public ProdottoException(String message, StatusJob statusJob, Throwable cause) {
    }
}