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

    private final Long id;
    private final SXFatturaStatus current;
    private final SXFatturaStatus expected;

    public InvalidFatturaStatusException(Long id, SXFatturaStatus current, SXFatturaStatus expected) {
        super(
                String.format(
                        "Stato fattura non valido (ID=%d, attuale=%s, richiesto=%s)",
                        id, current, expected
                ),
                StatusJob.FAILED
        );

        this.id = id;
        this.current = current;
        this.expected = expected;

        log.error(
                "[InvalidFatturaStatusException] id={} current={} expected={}",
                id, current, expected
        );
    }
}