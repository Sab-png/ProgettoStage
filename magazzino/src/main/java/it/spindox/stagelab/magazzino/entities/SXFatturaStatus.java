package it.spindox.stagelab.magazzino.entities;

import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;

@Getter
public enum SXFatturaStatus {

    EMESSA("ISSUED", "Fattura emessa e non ancora saldata"),
    SCADUTA("OVERDUE", "Fattura non saldata entro la data di scadenza"),
    PAGATA("PAID", "Fattura completamente saldata");

    // Valore da salvare in DB
    private final String dbValue;

    // Descrizione leggibile
    private final String description;

    SXFatturaStatus(String dbValue, String description) {
        this.dbValue = dbValue;
        this.description = description;
    }

    // Determina lo stato in base ai valori

    public static SXFatturaStatus determine(
            BigDecimal importo,
            BigDecimal pagato,
            LocalDate dataScadenza
    ) {
        if (importo == null) {
            throw new IllegalArgumentException("Importo nullo!");
        }

        LocalDate today = LocalDate.now(ZoneId.of("Europe/Rome"));

        if (pagato != null && pagato.compareTo(importo) >= 0) {
            return PAGATA;
        }

        if (dataScadenza != null && today.isAfter(dataScadenza)) {
            return SCADUTA;
        }

        return EMESSA;
    }

    // Conversione DB A  Enum

    public static SXFatturaStatus fromDbValue(String dbData) {
        if (dbData == null) return null;

        for (SXFatturaStatus status : values()) {
            if (status.getDbValue().equalsIgnoreCase(dbData)) {
                return status;
            }
        }

        throw new IllegalArgumentException("Valore stato non riconosciuto: " + dbData);
    }
}