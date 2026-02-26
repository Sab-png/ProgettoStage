package it.spindox.stagelab.magazzino.entities;
import lombok.Getter;

@Getter
public enum ScortaMinPMStatus {

    VERDE("GREEN",  "Scorta minima elevata"),
    GIALLO("YELLOW","Scorta minima bassa"),
    ROSSO("RED",    "Scorta minima critica/assente");

    private final String code;
    private final String description;

    ScortaMinPMStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }


     // Calcolo stato SOLO in base alla QUANTITA:
     // ROSSO  se quantita == null o quantita <= 0
     // GIALLO se 1 <= quantita <= 5
     // VERDE  se quantita > 5

    public static ScortaMinPMStatus fromQuantita(Integer quantita) {
        if (quantita == null || quantita <= 0) return ROSSO;
        if (quantita <= 5) return GIALLO;
        return VERDE;
    }

    public static ScortaMinPMStatus from(Integer scortaMin) {
        return null;
    }
}