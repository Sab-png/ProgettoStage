package it.spindox.stagelab.magazzino.exceptions.prodottomagazzinoexceptions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter

public class CapacityExceededException extends ProdottoMagazzinoException {

    private final Long magazzinoId;
    private final Integer capacita;
    private final Integer totaleRichiesto;

    public CapacityExceededException(Long magazzinoId, Integer capacita, Integer totaleRichiesto) {
        super("Capacità del magazzino superata: magazzinoId=" + magazzinoId +
                ", capacità=" + capacita + ", richiesto=" + totaleRichiesto);

        this.magazzinoId = magazzinoId;
        this.capacita = capacita;
        this.totaleRichiesto = totaleRichiesto;

        log.error("[PM-CAPACITY] magazzinoId={} capacita={} totaleRichiesto={}",
                magazzinoId, capacita, totaleRichiesto);
    }
}