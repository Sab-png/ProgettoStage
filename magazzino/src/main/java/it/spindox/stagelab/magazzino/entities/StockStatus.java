package it.spindox.stagelab.magazzino.entities;


// ENUMERATION CARATTERISTICHE


public enum StockStatus {

    VERDE("GREEN", "Stock normale", Severity.OK),
    GIALLO("YELLOW", "Stock sotto soglia", Severity.WARN),
    ROSSO("RED", "Stock esaurito", Severity.ERROR);

    private final String dbValue;

     StockStatus(String dbValue, String description, Severity severity) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return "";
    }

    public enum Severity { OK, WARN, ERROR }

    public static StockStatus fromQuantita(Integer quantita, int soglia) {
        if (quantita == null) return null;
        if (quantita == 0) return ROSSO;
        if (quantita < soglia) return GIALLO;
        return VERDE;
    }

    public static StockStatus fromDbValue(String value) {
        if (value == null) return null;
        for (StockStatus s : values()) {
            if (s.dbValue.equalsIgnoreCase(value)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Valore DB non valido: " + value);
    }
}
