package it.spindox.stagelab.magazzino.exceptions.prodottoexceptions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
public class InvalidScortaMinimaException extends ProdottoException {
    public InvalidScortaMinimaException(Long prodottoId, Integer scortaMin) {
        super("Scorta minima non valida (" + scortaMin + ") per il prodotto ID=" + prodottoId);
    }
}