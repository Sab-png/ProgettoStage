package it.spindox.stagelab.magazzino.exceptions.fatturaexceptions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;



// Errore nelle date: emissione futura o scadenza precedente all’emissione


@Slf4j
@Getter

public class InvalidDataFatturaException extends SXFatturaException {

  private final Long idFattura;
  private final LocalDate dataFattura;
  private final LocalDate dataScadenza;

  public InvalidDataFatturaException(Long idFattura, LocalDate dataFattura, LocalDate dataScadenza) {
    super("La data di scadenza non può essere precedente alla data della fattura");
    this.idFattura = idFattura;
    this.dataFattura = dataFattura;
    this.dataScadenza = dataScadenza;
    log.error("[InvalidDataFatturaException] id={} dataFattura={} dataScadenza={}",
            idFattura, dataFattura, dataScadenza);
  }
}