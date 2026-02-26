package it.spindox.stagelab.magazzino.entities;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDate;


// Enum che rappresenta lo stato della fattura
// Metodo determine:  responsabile della logica


@Getter
public enum SXFatturaStatus {

    EMESSA("ISSUED", "Fattura emessa e non ancora saldata"),
    SCADUTA("OVERDUE", "Fattura non saldata entro la data di scadenza"),
    PAGATA("PAID", "Fattura completamente saldata");

    // Valore da salvare in DB se in futuro

    private final String dbValue;


    private final String description;

    SXFatturaStatus(String dbValue, String description) {
        this.dbValue = dbValue;
        this.description = description;
    }

    // Metodo statico per determinare lo stato della fattura in base a importo, pagato e dataScadenza

    public static SXFatturaStatus determine(
            BigDecimal importo,
            BigDecimal pagato,
            LocalDate dataScadenza
    ) {
        if (importo == null) {
            throw new IllegalArgumentException("Importo nullo!");
        }

        // Caso 1 : fattura pagata

        if (pagato != null && pagato.compareTo(importo) >= 0) {
            return PAGATA;
        }

        // Caso 2 : fattura scaduta

        if (dataScadenza != null && LocalDate.now().isAfter(dataScadenza)) {
            return SCADUTA;
        }

        // Caso 3 : stato Emessa

        return EMESSA;
    }

}