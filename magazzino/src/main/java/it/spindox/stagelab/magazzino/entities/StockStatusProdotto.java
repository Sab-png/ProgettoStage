package it.spindox.stagelab.magazzino.entities;


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

    public static StockStatusProdotto fromDbValue(String dbValue) {
        return null;
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

    public static StockStatusProdotto fromQuantita(Integer quantita, Integer scortaMin) {
        if (quantita == null || quantita == 0) return ROSSO;
        if (scortaMin != null && quantita < scortaMin) return GIALLO;
        return VERDE;
    }
}