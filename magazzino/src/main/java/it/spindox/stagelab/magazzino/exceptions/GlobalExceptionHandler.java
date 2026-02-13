
package it.spindox.stagelab.magazzino.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;



@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ERRORE 404 : STATUS NOT FOUND

    @ExceptionHandler(ResourceNotFoundException.class)

    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {

        log.warn("Pagina/Risorsa non trovata: {}", ex.getMessage());

        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // ERRORE 500 : INTERNAL SERVER ERROR : ERRORE INATTESO

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericError(Exception ex, HttpServletRequest request) {
        log.error("Errore inatteso", ex);

        return ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Si è verificato un errore interno. Riprova più tardi."
        );
    }
}