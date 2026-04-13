package it.spindox.stagelab.magazzino.exceptions;

public class ReservationExpiredException extends RuntimeException {
    public ReservationExpiredException(String message) {
        super(message);
    }
}