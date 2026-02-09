package it.spindox.stagelab.magazzino.entities;

public enum SJobErrorType {
    BUSINESS_ERROR,     // errore di dominio
    VALIDATION_ERROR,   // dati non validi
    TECHNICAL_ERROR,    // DB, rete, timeout
    SYSTEM_ERROR,       // crash, NPE, OOM
    EXTERNAL_SERVICE,   // chiamata a servizi esterni
    UNKNOWN             // fallback
}

