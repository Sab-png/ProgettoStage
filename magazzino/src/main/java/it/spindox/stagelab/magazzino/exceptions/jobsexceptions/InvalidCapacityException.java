package it.spindox.stagelab.magazzino.exceptions.jobsexceptions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import it.spindox.stagelab.magazzino.entities.StatusJobErrorType;
import org.springframework.http.HttpStatus;


// Eccezione unica per valori numerici non validi:
// quantita negativa (Prodotto/Prodotto-Magazzino)
// capacita magazzino negativa
// valore DB non valido


// Eccezione unica per valori numerici non validi:
// quantita negativa (Prodotto/Prodotto-Magazzino)
// capacita magazzino negativa
// valore DB non valido


@Slf4j
@Getter
public class InvalidCapacityException extends JobException {

    private final Long prodottoId;        // opzionale : caso prodotto
    private final Integer quantita;       // opzionale : caso prodotto
    private final String nomeMagazzino;   // opzionale :  caso magazzino
    private final Integer capacity;       // opzionale : caso magazzino
    private final String dbValue;         // opzionale :  caso DB


    // 1) CASO PRODOTTO / PRODOTTO-MAGAZZINO  : nel caso la quantita negativa/null

    public InvalidCapacityException(Long prodottoId, Integer quantita, String message) {
        super(message, StatusJobErrorType.SYSTEM_ERROR, HttpStatus.BAD_REQUEST);

        this.prodottoId = prodottoId;
        this.quantita = quantita;

        this.nomeMagazzino = null;
        this.capacity = null;
        this.dbValue = null;

        log.error("[INVALID CAPACITY/QUANTITY] prodottoId={} quantita={} msg={}",
                prodottoId, quantita, message);
    }


    // 2) CASO MAGAZZINO : capacità negativa

    public InvalidCapacityException(String nomeMagazzino, Integer capacity, String message) {
        super("La capacità del magazzino '" + nomeMagazzino +
                        "' non può essere negativa: " + capacity,
                StatusJobErrorType.SYSTEM_ERROR,
                HttpStatus.BAD_REQUEST);

        this.nomeMagazzino = nomeMagazzino;
        this.capacity = capacity;

        this.prodottoId = null;
        this.quantita = null;
        this.dbValue = null;

        log.error("[INVALID CAPACITY-MAGAZZINO] magazzino='{}' capacity={}",
                nomeMagazzino, capacity);
    }


    // 3) CASO DB VALUE NON VALIDO

    public InvalidCapacityException(String dbValue, String message) {
        super(message, StatusJobErrorType.SYSTEM_ERROR, HttpStatus.BAD_REQUEST);

        this.dbValue = dbValue;

        this.prodottoId = null;
        this.quantita = null;
        this.nomeMagazzino = null;
        this.capacity = null;

        log.error("[INVALID DB VALUE] dbValue={} msg={}",
                dbValue, message);
    }
}