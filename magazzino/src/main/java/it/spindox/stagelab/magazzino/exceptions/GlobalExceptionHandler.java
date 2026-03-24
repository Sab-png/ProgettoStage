
package it.spindox.stagelab.magazzino.exceptions;
import it.spindox.stagelab.magazzino.exceptions.jobsexceptions.FatturaWorkExecutionException;
import it.spindox.stagelab.magazzino.exceptions.jobsexceptions.JobException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import java.time.OffsetDateTime;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.LinkedHashMap;
import java.util.Map;



@RestControllerAdvice

public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String PATH = "path";
    private static final String TIMESTAMP = "timestamp";


    // 404 - RISORSA NON TROVATA

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {

        log.warn("Risorsa non trovata su path {}: {}", request.getRequestURI(), ex.getMessage());

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );

        pd.setProperty(PATH, request.getRequestURI());
        pd.setProperty(TIMESTAMP, OffsetDateTime.now().toString());
        return pd;
    }


    // 400 - BAD REQUEST GENERICO

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {

        log.warn("Bad request su path {}: {}", request.getRequestURI(), ex.getMessage());

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );

        pd.setProperty(PATH, request.getRequestURI());
        pd.setProperty(TIMESTAMP, OffsetDateTime.now().toString());
        return pd;
    }


    // 400 - VALIDAZIONE @Valid SU @RequestBody (DTO di search)

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {

        log.warn("Validation error su path {}: {}", request.getRequestURI(), ex.getMessage());

        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Richiesta non valida: errori di validazione."
        );

        pd.setProperty("errors", errors);
        pd.setProperty(PATH, request.getRequestURI());
        pd.setProperty(TIMESTAMP, OffsetDateTime.now().toString());
        return pd;
    }


    // 400 - BODY NON LEGGIBILE / JSON MALFORMATO / proprietà sconosciute


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {

        log.warn("Body non leggibile su path {}: {}", request.getRequestURI(), ex.getMessage());

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Body non leggibile o formattazione JSON non valida."
        );

        pd.setProperty(PATH, request.getRequestURI());
        pd.setProperty(TIMESTAMP, OffsetDateTime.now().toString());
        return pd;
    }


    // Gestore centralizzato per le eccezioni del dominio FatturaWorkExecution

//  - BUSINESS_ERROR :  400 (BAD_REQUEST)
//  - BUSINESS_WARNING : 200 (OK)
//  - SYSTEM_ERROR :  500 (INTERNAL_SERVER_ERROR)
//  - UNKNOWN : 500 (INTERNAL_SERVER_ERROR)
//  - ERRORI SULLA PARTE DEL WEBCLIENT


    @ExceptionHandler(FatturaWorkExecutionException.class)
    public ProblemDetail handleFatturaWorkExecution(FatturaWorkExecutionException ex,
                                                    HttpServletRequest request) {

        // PER IL TRACKING

        log.error("[FATTURA_WORK_EXEC_ERROR] type={} http={} msg={} path={}",
                ex.getFatturaErrorType(), ex.getHttpStatus(), ex.getMessage(), request.getRequestURI());

        // Costruisce un ProblemDetail

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                ex.getHttpStatus(),   // BAD_REQUEST / OK / INTERNAL_SERVER_ERROR
                ex.getMessage()       // messaggio dell’errore (o di warning)
        );

        // tipo fattura
        pd.setTitle(ex.getFatturaErrorType().name());

        // VARI DATI INERENTI ALLA FATTURA

        pd.setProperty("fatturaErrorType", ex.getFatturaErrorType().name());                 // es: BUSINESS_WARNING
        pd.setProperty("fatturaId", ex.getFatturaId());                               // id fattura
        pd.setProperty("workExecutionId", ex.getWorkExecutionId());                         // id work-exec
        pd.setProperty("executionStatus", ex.getExecutionStatus() != null ? ex.getExecutionStatus().name() : null); // SUCCESS/ERROR/FAILED
        pd.setProperty("details", ex.getDetails());                                  // es: "dataScadenza=2026-01-31"
        pd.setProperty("occurredAt", ex.getOccurredAt().toString());                    // timestamp errore/warning
        pd.setProperty("path", request.getRequestURI());
        pd.setProperty("timestamp", java.time.OffsetDateTime.now().toString());

        return pd;
    }

    // ERRORE 409 PER JOBEXCEPTION : InvalidCapacityException, InvalidFatturaException, UnknownJobException


    @ExceptionHandler(JobException.class)
    public ProblemDetail handleJobException(JobException ex, HttpServletRequest request) {

        log.error("JOB ERROR — type={}, status={}, msg={}, path={}",
                ex.getErrorType(), ex.getHttpStatus(), ex.getMessage(), request.getRequestURI(), ex);

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                ex.getHttpStatus(),
                ex.getMessage()
        );

        pd.setProperty("errorType", ex.getErrorType().name());
        pd.setProperty(PATH, request.getRequestURI());
        pd.setProperty(TIMESTAMP, OffsetDateTime.now().toString());
        return pd;
    }


    // 500 - FALLBACK GENERICO


    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericError(Exception ex, HttpServletRequest request) {

        log.error("Errore inatteso su path {}", request.getRequestURI(), ex);

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Si è verificato un errore interno. Riprova più tardi."
        );

        pd.setProperty(PATH, request.getRequestURI());
        pd.setProperty(TIMESTAMP, OffsetDateTime.now().toString());
        return pd;
    }

    // WEBCLIENT ERROR TIPOLOGIE ERRORI PER API ESTERNO : USERS

    @ExceptionHandler(WebClientResponseException.class)
    public ProblemDetail handleWebClientError(
            WebClientResponseException ex,
            HttpServletRequest request) {

        log.error("Errore WebClient su path {}: {}", request.getRequestURI(), ex.getMessage());

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                ex.getStatusCode(),
                "Errore nella chiamata al servizio utenti: " + ex.getMessage()
        );

        pd.setProperty("path", request.getRequestURI());
        pd.setProperty("timestamp", OffsetDateTime.now().toString());
        pd.setProperty("service", "userClient");

        // Se l'endpoint contiene 'user-by-name' lo segnalo

        if (request.getRequestURI().contains("user-by-name")) {
            pd.setProperty("lookupType", "NAME");
        }

        // Se l'endpoint contiene 'user' classico lo segnalo

        if (request.getRequestURI().contains("user-by-username")) {
            pd.setProperty("lookupType", "USERNAME");
        }

        return pd;
    }
}