package it.spindox.stagelab.magazzino.entities;

/** Commenti e implementazioni varie
 * Stato dello stock:
 * - VERDE  : quantita >= soglia
 * - GIALLO : 0 < quantita =< soglia
 * - ROSSO  : quantita == 0
 */

public enum StockStatus {
    VERDE("Stock normale", Severity.OK),
    GIALLO("Stock sotto soglia", Severity.WARN),
    ROSSO("Stock esaurito", Severity.ERROR);

    private final String description;
    private final Severity severity;

    StockStatus(String description, Severity severity) {
        this.description = description;
        this.severity = severity;
    }

    public String description() { return description; }

    public Severity severity() { return severity; }

    public enum Severity { OK, WARN, ERROR }

    /**
     *   Stato dato quantita e soglia.
     * - quantita == null → restituisce null (non valutabile)
     * - quantita == 0    → ROSSO
     * - quantita < soglia→ GIALLO
     * - altrimenti       → VERDE (cioè quantita >= soglia)
     */

    public static StockStatus fromQuantita(Integer quantita, int soglia) {
        if (quantita == null) return null;
        if (quantita == 0) return ROSSO;
        if (quantita <= soglia) return GIALLO;
        return VERDE; // quantita > soglia
    }
}