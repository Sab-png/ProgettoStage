
package it.spindox.stagelab.magazzino.exceptions;
import it.spindox.stagelab.magazzino.exceptions.fatturaexceptions.SXFatturaException;
import it.spindox.stagelab.magazzino.exceptions.jobsexceptions.JobException;
import it.spindox.stagelab.magazzino.exceptions.prodottoexceptions.ProdottoException;
import it.spindox.stagelab.magazzino.exceptions.prodottomagazzinoexceptions.ProdottoMagazzinoException;
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


    // 400 - ERRORI DI VALIDAZIONE PRODOTTO
    @ExceptionHandler(ProdottoException.class)
    public ProblemDetail handleProdottoException(ProdottoException ex, HttpServletRequest request) {

        log.warn("Errore di validazione prodotto su path {}: {}", request.getRequestURI(), ex.getMessage());

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
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


    // JOBEXCEPTION : status dinamico + errorType

    @ExceptionHandler(JobException.class)
    public ProblemDetail handleJobException(JobException ex, HttpServletRequest request) {

        log.error("JOB ERROR — type={}, status={}, msg={}, path={}",
                ex.getErrorType(),
                ex.getHttpStatus(),
                ex.getMessage(),
                request.getRequestURI(),
                ex
        );

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                ex.getHttpStatus(),
                ex.getMessage()
        );

        pd.setProperty("errorType", ex.getErrorType().name());
        pd.setProperty(PATH, request.getRequestURI());
        pd.setProperty(TIMESTAMP, OffsetDateTime.now().toString());

        return pd;
    }


    // 500 - ERRORI NON PREVISTI

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
    // ECCEZZIONI FATTURA ERRORE 400 : BAD REQUEST

    @ExceptionHandler(SXFatturaException.class)
    public ProblemDetail handleFatturaException(SXFatturaException ex, HttpServletRequest request) {

        log.warn("Errore fattura su path {}: {}", request.getRequestURI(), ex.getMessage());

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );

        pd.setProperty("path", request.getRequestURI());
        pd.setProperty(TIMESTAMP, OffsetDateTime.now().toString());

        return pd;
    }
    // 400 - ERRORI DI BUSINESS PRODOTTO-MAGAZZINO

    @ExceptionHandler(ProdottoMagazzinoException.class)
    public ProblemDetail handleProdottoMagazzinoException(ProdottoMagazzinoException ex, HttpServletRequest request) {

        log.warn("Errore Prodotto-Magazzino su path {}: {}", request.getRequestURI(), ex.getMessage());

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );

        pd.setProperty(PATH, request.getRequestURI());
        pd.setProperty(TIMESTAMP, OffsetDateTime.now().toString());

        return pd;
    }
}
