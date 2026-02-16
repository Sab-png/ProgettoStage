package it.spindox.stagelab.magazzino.exceptions.prodottoexceptions;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
public class InvalidScortaMinimaException extends ProdottoException {

    // Messaggio generato automaticamente

    public InvalidScortaMinimaException(Long prodottoId, Integer scortaMin) {
        super("Scorta minima non valida (" + scortaMin + ") per il prodotto ID=" + prodottoId,
                StatusJob.FAILED);
        log.error("[InvalidSortaMinimaException] prodottoId={}, scortaMin={}", prodottoId, scortaMin);
    }

    // Messaggio personalizzato

    public InvalidScortaMinimaException(Long prodottoId, Integer scortaMin, String message) {
        super(message, StatusJob.FAILED);
        log.error("[InvalidScortaMinimaException] prodottoId={}, scortaMin={}, msg={}",
                prodottoId, scortaMin, message);
    }
}