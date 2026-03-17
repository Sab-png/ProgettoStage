package it.spindox.stagelab.magazzino.entities;
import it.spindox.stagelab.magazzino.exceptions.jobsexceptions.InvalidCapacityException;
import lombok.Getter;
import java.util.Arrays;



@Getter
public enum StockStatusProdotto {

    VERDE("GREEN", "Stock nella norma"),
    GIALLO("YELLOW", "Sotto la scorta minima"),
    ROSSO("RED", "Esaurito");

    private final String dbValue;
    private final String description;

    StockStatusProdotto(String dbValue, String description) {
        this.dbValue = dbValue;
        this.description = description;
    }

    // Conversione dal valore del DB

    public static StockStatusProdotto fromDbValue(String dbValue) {

        if (dbValue == null) {
            throw new InvalidCapacityException("NULL", "Valore DB nullo per StockStatusProdotto");
        }

        return Arrays.stream(values())
                .filter(s -> s.getDbValue().equalsIgnoreCase(dbValue.trim()))
                .findFirst()
                .orElseThrow(() ->
                        new InvalidCapacityException(
                                dbValue,
                                "Valore DB non valido per StockStatusProdotto"
                        )
                );
    }

    // Stato calcolato

    public static StockStatusProdotto fromQuantita(Integer quantita, Integer scortaMin) {

        if (quantita == null || quantita <= 0)
            return ROSSO;

        if (scortaMin != null && quantita < scortaMin)
            return GIALLO;

        return VERDE;
    }
}