package it.spindox.stagelab.magazzino.entities;

public enum StockStatusMagazzino {

    VERDE("GREEN", "Magazzino con scorte sufficienti", Severity.OK),
    GIALLO("YELLOW", "Magazzino in riduzione scorte", Severity.WARN),
    ROSSO("RED", "Magazzino quasi vuoto", Severity.ERROR);

    private final String dbValue;
    private final String description;
    private final Severity severity;

    StockStatusMagazzino(String dbValue, String description, Severity severity) {
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

    /**
     * percentuale capacità = (totale/capacità)*100
     * Logica:
     * - <= 5%      → ROSSO
     * - 5%–20%     → GIALLO
     * - 20%–50%    → GIALLO
     * - >= 50%     → VERDE
     */
    public static StockStatusMagazzino fromPercentuale(double percent) {
        if (percent <= 5) return ROSSO;
        if (percent < 50) return GIALLO;
        return VERDE;
    }
}