package it.spindox.stagelab.magazzino.entities;
import lombok.Getter;

// tracking job fatture

@Getter

public enum SXFatturaJobexecution {


    RUNNING,
    SUCCESS,
    FAILED,
    ERROR
}