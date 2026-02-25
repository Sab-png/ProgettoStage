package it.spindox.stagelab.magazzino.entities;
import it.spindox.stagelab.magazzino.exceptions.jobsexceptions.InvalidCapacityException;
import lombok.Getter;
import java.util.Arrays;

@Getter

public enum StockStatusProdotto {

    VERDE("GREEN", "Stock nella norma"),
    GIALLO("YELLOW", "Sotto la scorta minima"),
    ROSSO("RED", "Esaurito");

    private static String message;
    @Getter
    private final String dbValue;
    @Getter
    private final String description;

    StockStatusProdotto(String dbValue, String description) {
        this.dbValue = dbValue;
        this.description = description;
    }

    // Stato ottenuto dal DB

    public static StockStatusProdotto fromDbValue(String dbValue) {
        return Arrays.stream(values())
                .filter(s -> s.getDbValue().equalsIgnoreCase(dbValue))
                .findFirst()
                .orElseThrow(() -> new InvalidCapacityException(dbValue, message));
    }

    // Stato calcolato da quantità e scorta minima

    public static StockStatusProdotto fromQuantita(Integer quantita, Integer scortaMin) {
        if (quantita == null || quantita == 0) return ROSSO;
        if (scortaMin != null && quantita < scortaMin) return GIALLO;
        return VERDE;
    }
}
