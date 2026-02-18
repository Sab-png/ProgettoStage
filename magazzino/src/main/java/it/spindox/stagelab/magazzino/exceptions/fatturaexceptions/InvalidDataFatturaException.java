package it.spindox.stagelab.magazzino.exceptions.fatturaexceptions;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;


// Errore nelle date : riguardante l' emissione futura e la  scadenza prima dell’emissione

@Slf4j
@Getter
public class InvalidDataFatturaException extends SXFatturaException {

  public InvalidDataFatturaException(Long id, LocalDate emissione, LocalDate scadenza) {
    super(
            "Date della fattura non valide per ID=" + id +
                    " (emissione=" + emissione + ", scadenza=" + scadenza + ")",
            StatusJob.FAILED
    );

    log.error("[InvalidDataFatturaException] id={} emissione={} scadenza={}",
            id, emissione, scadenza);
  }
}