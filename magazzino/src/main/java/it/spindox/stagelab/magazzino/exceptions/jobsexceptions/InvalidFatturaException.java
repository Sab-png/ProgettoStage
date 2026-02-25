package it.spindox.stagelab.magazzino.exceptions.jobsexceptions;
import it.spindox.stagelab.magazzino.entities.StatusJobErrorType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import java.math.BigDecimal;
import java.time.LocalDate;



@Slf4j
@Getter
public class InvalidFatturaException extends JobException {

    private final Long idFattura;
    private final LocalDate dataFattura;
    private final LocalDate dataScadenza;
    private final BigDecimal importo;
    private final Integer quantita;



    // 1) ERRORE DATE – data scadenza incoerente rispetto alla data fattura

    public InvalidFatturaException(Long idFattura,
                                   LocalDate dataFattura,
                                   LocalDate dataScadenza) {

        super("La data di scadenza non può essere precedente o incoerente rispetto alla data della fattura",
                StatusJobErrorType.SYSTEM_ERROR,
                HttpStatus.BAD_REQUEST);

        this.idFattura = idFattura;
        this.dataFattura = dataFattura;
        this.dataScadenza = dataScadenza;
        this.importo = null;
        this.quantita = null;

        log.error("[InvalidFattura-DATA] id={} dataFattura={} dataScadenza={}",
                idFattura, dataFattura, dataScadenza);
    }


      // 2) ERRORE STATUS :  fattura già pagata

    public InvalidFatturaException(Long idFattura) {

        super("La fattura con id " + idFattura + " è già pagata e non può essere modificata",
                StatusJobErrorType.SYSTEM_ERROR,
                HttpStatus.BAD_REQUEST);

        this.idFattura = idFattura;
        this.dataFattura = null;
        this.dataScadenza = null;
        this.importo = null;
        this.quantita = null;

        log.error("[InvalidFattura-STATUS] id={} status=PAGATA (update negato)", idFattura);
    }


     // 3) ERRORE IMPORTO/QUANTITÀ – importo o quantita non validi

    public InvalidFatturaException(Long idFattura,
                                   BigDecimal importo,
                                   Integer quantita) {

        super("Importo o quantità non validi: importo=" + importo + ", quantita=" + quantita,
                StatusJobErrorType.SYSTEM_ERROR,
                HttpStatus.BAD_REQUEST);

        this.idFattura = idFattura;
        this.dataFattura = null;
        this.dataScadenza = null;
        this.importo = importo;
        this.quantita = quantita;

        log.error("[InvalidFattura-IMPORTO] id={} importo={} quantita={}",
                idFattura, importo, quantita);
    }
}