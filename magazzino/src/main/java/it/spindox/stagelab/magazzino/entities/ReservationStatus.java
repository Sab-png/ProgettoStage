package it.spindox.stagelab.magazzino.entities;

public enum ReservationStatus {
    RESERVED,   // Prenotato - stock riservato
    EXPIRED,    // Scaduto - sessione terminata
    COMPLETED   // Completato - ordine pagato
}