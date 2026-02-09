package it.spindox.stagelab.magazzino.entities;

public enum StatusJob {
    PENDING(false),
    RUNNING(false),
    SUCCESS(true),
    FAILED(true),
    ERROR(true),
    CANCELLED(true);

    private final boolean terminal;

    StatusJob(boolean terminal) {
        this.terminal = terminal;
    }

}
