package it.spindox.stagelab.magazzino.exceptions.fatturaexceptions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;


// REGOLE SU IMPORTI/QUANTITA' : NON NEGATIVI, NON NULL QUINDI UN TOTAL CON UN MINIMO DI SENSO

@Slf4j
@Getter

public class InvalidImportoFatturaException extends SXFatturaException {

    private final Long idFattura;
    private final BigDecimal importo;
    private final Integer quantita;

    public InvalidImportoFatturaException(Long idFattura, BigDecimal importo, Integer quantita) {
        super("Importo fattura non valido: importo=" + importo + ", quantita=" + quantita);
        this.idFattura = idFattura;
        this.importo = importo;
        this.quantita = quantita;
        log.error("[InvalidImportoFatturaException] id={} importo={} quantita={}",
                idFattura, importo, quantita);
    }
}