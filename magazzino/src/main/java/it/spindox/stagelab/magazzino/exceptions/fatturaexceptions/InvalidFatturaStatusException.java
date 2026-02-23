package it.spindox.stagelab.magazzino.exceptions.fatturaexceptions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


// Quando lo stato della fattura non è coerente con : importi, scadenza o se e' gia' stata pagata

// È la versione per SXFatturaStatus di : EMESSA, SCADUTA, PAGATA

@Slf4j
@Getter

public class InvalidFatturaStatusException extends SXFatturaException {

    private final Long idFattura;

    public InvalidFatturaStatusException(Long idFattura) {
        super("La fattura con id " + idFattura + " è già pagata e non può essere modificata");
        this.idFattura = idFattura;
        log.error("[InvalidFatturaStatusException] id={} status=PAGATA (update negato)", idFattura);
    }
}