package it.spindox.stagelab.magazzino.exceptions.fatturaexceptions;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;



// Errore nelle date: emissione futura o scadenza precedente all’emissione

@Slf4j
@Getter

public class InvalidDataFatturaException extends SXFatturaException {

  private final Long id;
  private final LocalDate emissione;
  private final LocalDate scadenza;

  public InvalidDataFatturaException(Long id, LocalDate emissione, LocalDate scadenza) {
    super(
            String.format(
                    "Date della fattura non valide (ID=%d, emissione=%s, scadenza=%s)",
                    id, emissione, scadenza
            ),
            StatusJob.FAILED
    );

    this.id = id;
    this.emissione = emissione;
    this.scadenza = scadenza;

    log.error(
            "[InvalidDataFatturaException] id={} emissione={} scadenza={}",
            id, emissione, scadenza
    );
  }
}
