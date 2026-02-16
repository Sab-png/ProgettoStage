package it.spindox.stagelab.magazzino.exceptions.magazzinoexceptions;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
public class ProductQuantityException extends MagazzinoException {
    public ProductQuantityException(Long idProdotto, String nomeMagazzino, Integer qta) {
        super("Quantità non valida (" + qta + ") per il prodotto " + idProdotto +
                        " nel magazzino '" + nomeMagazzino + "'",
                StatusJob.FAILED);
    }
}
