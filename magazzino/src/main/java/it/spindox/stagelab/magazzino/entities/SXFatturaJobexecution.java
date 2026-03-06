package it.spindox.stagelab.magazzino.entities;
import lombok.Getter;



@Getter
public enum SXFatturaJobexecution {

    PENDING,
    RUNNING,
    SUCCESS,
    FAILED,
    ERROR
}