package it.spindox.stagelab.magazzino.exceptions.jobsexceptions;
import it.spindox.stagelab.magazzino.entities.SXFatturaJobexecution;
import it.spindox.stagelab.magazzino.entities.SXFatturaJobexecutionErrorType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import java.time.OffsetDateTime;


 // Eccezione runtime dedicata al dominio FatturaWorkExecution.
 // NON estende JobException (è isolata)


@Slf4j
@Getter
public class FatturaWorkExecutionException extends RuntimeException {

    private final SXFatturaJobexecutionErrorType fatturaErrorType;
    private final HttpStatus httpStatus;
    private final Long fatturaId;
    private final Long workExecutionId;
    private final SXFatturaJobexecution executionStatus;
    private final String details;
    private final OffsetDateTime occurredAt;

    // EXECUTION DELL' EXCEPTION

    public FatturaWorkExecutionException(
            SXFatturaJobexecutionErrorType fatturaErrorType,
            String message,
            String details,
            Long fatturaId,
            Long workExecutionId,
            SXFatturaJobexecution executionStatus
    ) {
        super(message != null ? message : fatturaErrorType.getDefaultMessage());
        this.fatturaErrorType = fatturaErrorType;
        this.httpStatus = fatturaErrorType.getDefaultHttpStatus(); // es: BUSINESS_ERROR -> 400, BUSINESS_WARNING -> 200
        this.fatturaId = fatturaId;
        this.workExecutionId = workExecutionId;
        this.executionStatus = executionStatus;
        this.details = details;
        this.occurredAt = OffsetDateTime.now();

        log.error("[FATTURA-WORK-RUNTIME] type={} http={} fatturaId={} workExecId={} status={} msg='{}' details={}",
                fatturaErrorType, httpStatus, fatturaId, workExecutionId, executionStatus, getMessage(), details);
    }

    //  metodi exception//

    public static FatturaWorkExecutionException businessError(String msg, String details,
                                                              Long fatturaId, Long execId) {
        return new FatturaWorkExecutionException(
                SXFatturaJobexecutionErrorType.BUSINESS_ERROR,
                msg, details, fatturaId, execId, SXFatturaJobexecution.FAILED
        );
    }

    // caso SCADUTA o ci sia un anomalia : status execution può restare SUCCESS nel tracking //


    // BUSINESS WARNING

    public static FatturaWorkExecutionException businessWarning(String msg, String details,
                                                                Long fatturaId, Long execId) {
        return new FatturaWorkExecutionException(
                SXFatturaJobexecutionErrorType.BUSINESS_WARNING,
                msg, details, fatturaId, execId, SXFatturaJobexecution.SUCCESS
        );
    }
// SYSTEM ERROR

    public static FatturaWorkExecutionException systemError(String msg, String details,
                                                            Long fatturaId, Long execId) {
        return new FatturaWorkExecutionException(
                SXFatturaJobexecutionErrorType.SYSTEM_ERROR,
                msg, details, fatturaId, execId, SXFatturaJobexecution.ERROR
        );
    }

    // UNKNOWN

    public static FatturaWorkExecutionException unknown(String msg, String details,
                                                        Long fatturaId, Long execId) {
        return new FatturaWorkExecutionException(
                SXFatturaJobexecutionErrorType.UNKNOWN,
                msg, details, fatturaId, execId, SXFatturaJobexecution.FAILED
        );
    }

    public SXFatturaJobexecutionErrorType getErrorType() {
        return null;
    }
}
