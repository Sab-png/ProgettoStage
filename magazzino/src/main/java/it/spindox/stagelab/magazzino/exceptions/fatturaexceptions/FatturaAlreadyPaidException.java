package it.spindox.stagelab.magazzino.exceptions.fatturaexceptions;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
public class FatturaAlreadyPaidException extends SXFatturaException {

    public FatturaAlreadyPaidException(Long fatturaId) {
        super("La fattura ID=" + fatturaId + " risulta essere già pagata.", StatusJob.FAILED);

        log.error("[FatturaAlreadyPaidException] fatturaId={}", fatturaId);
    }
}