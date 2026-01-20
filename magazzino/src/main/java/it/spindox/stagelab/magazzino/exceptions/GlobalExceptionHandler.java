
package it.spindox.stagelab.magazzino.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Classe di gestione globale delle eccezioni REST.
 *
 * @RestControllerAdvice permette a Spring di intercettare
 * automaticamente le eccezioni lanciate dai controller/service
 * e convertirle in risposte HTTP appropriate.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gestisce l'eccezione ResourceNotFoundException.
     * Viene sollevata dal Service quando una risorsa
     * richiesta non è presente a database.
     *
     * @param ex      eccezione lanciata
     * @return ResponseEntity con status 404 e messaggio di errore
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String>  handleResourceNotFound(
            ResourceNotFoundException ex
    ) {
        // Restituisce una risposta HTTP 404 (Not Found)
        // con il messaggio definito nell'eccezione
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
}
