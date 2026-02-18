package it.spindox.stagelab.magazzino.exceptions.jobsexceptions;
import it.spindox.stagelab.magazzino.entities.StatusJobErrorType;
import lombok.Getter;
import org.springframework.http.HttpStatus;



 // Tutte le eccezioni  erediteranno da questa classe
// Run time exception non sono obbligato a dichiararla perche' e' gia implementata e dichiarata da java

@Getter

public abstract class JobException extends RuntimeException {

     // Tipo di errore del job

    private final StatusJobErrorType errorType;

     // Lo status HTTP che il GlobalExceptionHandler restituirà al client

    private final HttpStatus httpStatus;

    // Restituisce la tipologia di errore associata all'eccezione
    // Restituisce lo status HTTP che rappresenta questo errore

    protected JobException(String message, StatusJobErrorType errorType, HttpStatus httpStatus) {
        super(message);
        this.errorType = errorType;
        this.httpStatus = httpStatus;
    }



}