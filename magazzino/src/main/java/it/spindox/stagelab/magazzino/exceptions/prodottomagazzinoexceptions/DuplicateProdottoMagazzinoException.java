package it.spindox.stagelab.magazzino.exceptions.prodottomagazzinoexceptions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
public class DuplicateProdottoMagazzinoException extends ProdottoMagazzinoException {

    private final Long prodottoId;
    private final Long magazzinoId;

    public DuplicateProdottoMagazzinoException(Long prodottoId, Long magazzinoId) {
        super("Relazione Prodotto-Magazzino già esistente: prodottoId=" +
                prodottoId + ", magazzinoId=" + magazzinoId);

        this.prodottoId = prodottoId;
        this.magazzinoId = magazzinoId;

        log.error("[PM-DUPLICATE] prodottoId={} magazzinoId={}", prodottoId, magazzinoId);
    }
}