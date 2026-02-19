package it.spindox.stagelab.magazzino.exceptions.fatturaexceptions;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;


// REGOLE SU IMPORTI/QUANTITA' : NON NEGATIVI, NON NULL QUINDI UN TOTAL CON UN MINIMO DI SENSO


@Slf4j
@Getter

public class InvalidImportoFatturaException extends SXFatturaException {

    private final Long id;
    private final BigDecimal importo;
    private final Integer quantita;

    public InvalidImportoFatturaException(Long id, BigDecimal importo, Integer quantita) {
        super(
                String.format(
                        "Importi fattura non validi (ID=%d, importo=%s, quantità=%d)",
                        id, importo, quantita
                ),
                StatusJob.FAILED
        );

        this.id = id;
        this.importo = importo;
        this.quantita = quantita;

        log.error(
                "[InvalidImportoFatturaException] id={} importo={} quantità={}",
                id, importo, quantita
        );
    }
}
