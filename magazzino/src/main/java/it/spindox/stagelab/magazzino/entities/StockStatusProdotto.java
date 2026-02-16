package it.spindox.stagelab.magazzino.entities;
import it.spindox.stagelab.magazzino.exceptions.prodottoexceptions.InvalidStockStatusValueException;
import java.util.Arrays;

public enum StockStatusProdotto {

    VERDE("GREEN", "Stock nella norma", Severity.OK),
    GIALLO("YELLOW", "Sotto la scorta minima", Severity.WARN),
    ROSSO("RED", "Esaurito", Severity.ERROR);

    private final String dbValue;
    private final String description;
    private final Severity severity;

    StockStatusProdotto(String dbValue, String description, Severity severity) {
        this.dbValue = dbValue;
        this.description = description;
        this.severity = severity;
    }

    public String getDbValue() {
        return dbValue;
    }

    public String getDescription() {
        return description;
    }

    public Severity getSeverity() {
        return severity;
    }

    public enum Severity { OK, WARN, ERROR }

    // Stato ottenuto dal DB
    public static StockStatusProdotto fromDbValue(String dbValue) {
        return Arrays.stream(values())
                .filter(s -> s.getDbValue().equalsIgnoreCase(dbValue))
                .findFirst()
                .orElseThrow(() -> new InvalidStockStatusValueException(dbValue));
    }

    // Stato calcolato da quantità e scorta minima

    public static StockStatusProdotto fromQuantita(Integer quantita, Integer scortaMin) {
        if (quantita == null || quantita == 0) return ROSSO;
        if (scortaMin != null && quantita < scortaMin) return GIALLO;
        return VERDE;
    }
}