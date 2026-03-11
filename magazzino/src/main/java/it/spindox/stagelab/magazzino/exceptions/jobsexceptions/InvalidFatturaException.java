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

    //  ERRORE GENERICO PER MESSAGGI DI VALIDAZIONE

    public InvalidFatturaException(String message) {
        super(message,
                StatusJobErrorType.SYSTEM_ERROR,
                HttpStatus.CONFLICT); // <-- 409

        this.idFattura = null;
        this.dataFattura = null;
        this.dataScadenza = null;
        this.importo = null;
        this.quantita = null;

        log.error("[InvalidFattura] {}", message);
    }

    // 1) ERRORE DATE : data scadenza incoerente

    public InvalidFatturaException(Long idFattura) {
        super("La data di scadenza non può essere precedente o incoerente rispetto alla data della fattura",
                StatusJobErrorType.SYSTEM_ERROR,
                HttpStatus.BAD_REQUEST);

        this.idFattura = idFattura;
        this.dataFattura = null;
        this.dataScadenza = null;
        this.importo = null;
        this.quantita = null;
    }

    // 2) ERRORE IMPORTO/QUANTITÀ : importo o quantita non validi

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
    }
}