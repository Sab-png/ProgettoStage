package it.spindox.stagelab.magazzino.exceptions.prodottoexceptions;
import lombok.Getter;
import java.io.Serial;



@Getter
public class ProdottoException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_MESSAGE = "Errore generico sul prodotto.";

    public ProdottoException() {
        super(DEFAULT_MESSAGE);
    }
}