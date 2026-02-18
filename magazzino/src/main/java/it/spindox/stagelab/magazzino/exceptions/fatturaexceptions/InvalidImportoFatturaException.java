package it.spindox.stagelab.magazzino.exceptions.fatturaexceptions;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;


// IMPORTI  NON VALIDI

@Slf4j
@Getter
public class InvalidImportoFatturaException extends SXFatturaException {

    public InvalidImportoFatturaException(Long id, BigDecimal importo, Integer quantita) {
        super(
                "Importi fattura non validi (ID=" + id +
                        ", importo=" + importo +
                        ", quantità=" + quantita + ")",
                StatusJob.FAILED
        );

        log.error("[InvalidImportoFatturaException] id={} importo={} quantità={}",
                id, importo, quantita);
    }
}