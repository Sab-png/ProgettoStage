package it.spindox.stagelab.magazzino.exceptions.magazzinoexceptions;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter

public class InvalidCapacityException extends MagazzinoException {
    public InvalidCapacityException(String nomeMagazzino, Integer capacity) {
        super("Capacità non valida per il magazzino '" + nomeMagazzino + "': " + capacity,
                StatusJob.FAILED);
    }
}

