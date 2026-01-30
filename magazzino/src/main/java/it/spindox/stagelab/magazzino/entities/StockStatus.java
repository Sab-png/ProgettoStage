package it.spindox.stagelab.magazzino.entities;

public enum StockStatus {
    VERDE,    // Tutto disponibile
    GIALLO,   // Sotto soglia ma > 0
    ROSSO     // Esaurito
}