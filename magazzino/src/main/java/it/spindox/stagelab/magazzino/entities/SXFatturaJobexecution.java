package it.spindox.stagelab.magazzino.entities;
import lombok.Getter;

// tracking job fatture

@Getter

public enum SXFatturaJobexecution {


    // Job in esecuzione
    RUNNING,

    //Job completato con successo
    SUCCESS,

    // Job terminato a causa di un errore
    FAILED,

    //Job terminato a causa di un errore di sistema

    ERROR,

}