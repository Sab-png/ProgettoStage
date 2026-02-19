
package it.spindox.stagelab.magazzino.exceptions;
import it.spindox.stagelab.magazzino.exceptions.jobsexceptions.JobException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import java.time.OffsetDateTime;


@RestControllerAdvice

public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);



    // 404 : RESOURCE NOT FOUND

    @ExceptionHandler(ResourceNotFoundException.class)

    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {

        log.warn("Pagina/Risorsa non trovata: {}", ex.getMessage());

        return ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
    }



    // JOBEXCEPTION: STATUS + ERRORTYPE


    @ExceptionHandler(JobException.class)

    public ProblemDetail handleJobException(JobException ex, HttpServletRequest request) {

        log.error("JOB ERROR — type={}, status={}, msg={}",
                ex.getErrorType(),
                ex.getHttpStatus(),
                ex.getMessage(),
                ex
        );

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                ex.getHttpStatus(),
                ex.getMessage()
        );

        pd.setProperty("errorType", ex.getErrorType().name());
        pd.setProperty("path", request.getRequestURI());
        pd.setProperty("timestamp", OffsetDateTime.now().toString());

        return pd;
    }



    // 500 - ERRORI GENERICI : FALLBACK

    @ExceptionHandler(Exception.class)

    public ProblemDetail handleGenericError(Exception ex, HttpServletRequest request) {

        log.error("Errore inatteso", ex);

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Si è verificato un errore interno. Riprova più tardi."
        );

        pd.setProperty("path", request.getRequestURI());
        pd.setProperty("timestamp", OffsetDateTime.now().toString());

        return pd;
    }
}