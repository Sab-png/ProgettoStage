package it.spindox.stagelab.magazzino.exceptions.fatturaexceptions;
import it.spindox.stagelab.magazzino.entities.SXFatturaStatus;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

// Quando lo stato della fattura non è coerente con : importi, scadenza o se e' gia' stata pagata

// È la versione per SXFatturaStatus di : EMESSA, SCADUTA, PAGATA

@Slf4j
@Getter
public class InvalidFatturaStatusException extends SXFatturaException {

    public InvalidFatturaStatusException(Long id, SXFatturaStatus current, SXFatturaStatus expected) {
        super(
                "Stato fattura non valido (ID=" + id +
                        ", attuale=" + current +
                        ", richiesto=" + expected + ")",
                StatusJob.FAILED
        );

        log.error("[InvalidFatturaStatusException] id={} current={} expected={}",
                id, current, expected);
    }
}