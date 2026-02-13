package it.spindox.stagelab.magazzino.entities;

public enum StatusJobErrorType {

    VALIDATION_ERROR,     // dati non validi
    CONFIGURATION_ERROR,  // configurazione errata o mancante
    TECHNICAL_ERROR,      // DB, rete, timeout
    SECURITY_ERROR,       // auth, token, certificati
    SYSTEM_ERROR,         // crash, NPE, OOM
    INTERRUPTED,          // job interrotto
    UNKNOWN               // fallback
}