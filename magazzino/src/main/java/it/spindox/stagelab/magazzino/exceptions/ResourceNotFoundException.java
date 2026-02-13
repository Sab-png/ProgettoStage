
package it.spindox.stagelab.magazzino.exceptions;


  // Eccezione utilizzata quando
 // una risorsa richiestA
  // non viene trovata a database.

 // Viene lanciata tipicamente nel Service layer
 // e intercettata dal GlobalExceptionHandler per
  // restituire un HTTP 404 (Not Found).

public class ResourceNotFoundException extends RuntimeException {


      // Costruttore che accetta un messaggio descrittivo
      // dell'errore (es. "Prodotto non trovato").
    // param message:  messaggio nel dettaglio dell'eccezione

    public ResourceNotFoundException(String message) {

        // Richiama il costruttore della RuntimeException
        // per impostare il messaggio dell'errore

        super(message);
    }
}
