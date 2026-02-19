
package it.spindox.stagelab.magazzino.exceptions;


//  Eccezione usata quando una risorsa richiesta non viene trovata a database.
 // Tipicamente lanciata nel Service layer e mappata a HTTP 404 dal GlobalExceptionHandler.

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, Object id) {
        super(resourceName + " non trovata: id=" + id);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(resourceName + " non trovata: " + fieldName + "=" + fieldValue);
    }

    // Factory methods per leggibilità nei service

    public static ResourceNotFoundException byId(String resourceName, Object id) {
        return new ResourceNotFoundException(resourceName, id);
    }

    public static ResourceNotFoundException byField(String resourceName, String fieldName, Object fieldValue) {
        return new ResourceNotFoundException(resourceName, fieldName, fieldValue);
    }
}