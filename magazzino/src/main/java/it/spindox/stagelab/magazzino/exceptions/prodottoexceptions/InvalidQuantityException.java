package it.spindox.stagelab.magazzino.exceptions.prodottoexceptions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
public class InvalidQuantityException extends ProdottoException {

    public InvalidQuantityException(Long prodottoId, Integer quantita) {
        super("Quantità non valida (" + quantita + ") per il prodotto ID=" + prodottoId);
        log.error("[InvalidQuantityException] prodottoId={}, quantita={}", prodottoId, quantita);
    }
}