package it.spindox.stagelab.magazzino.entities;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
public enum SXFatturaStatus {

    EMESSA("ISSUED", "Fattura emessa e non ancora saldata"),
    SCADUTA("OVERDUE", "Fattura non saldata entro la data di scadenza"),
    PAGATA("PAID", "Fattura completamente saldata");

    private final String dbValue;
    private final String description;

    SXFatturaStatus(String dbValue, String description) {
        this.dbValue = dbValue;
        this.description = description;
    }


    // Logica  basata sull ' importo :  pagato, scadenza

    public static SXFatturaStatus fromDati(
            double importo,
            double pagato,
            LocalDate scadenza
    ) {
        if (pagato >= importo) return PAGATA;

        if (scadenza != null && LocalDate.now().isAfter(scadenza)) {
            return SCADUTA;
        }

        return EMESSA;
    }

    public static SXFatturaStatus fromDati(@NotNull(message = "L'importo è obbligatorio") @Positive(message = "L'importo deve essere maggiore di zero") BigDecimal importo, double pagato, LocalDate dataScadenza) {
        return null;
    }
}
