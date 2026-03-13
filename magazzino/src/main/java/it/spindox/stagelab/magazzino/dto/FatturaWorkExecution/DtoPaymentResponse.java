package it.spindox.stagelab.magazzino.dto.FatturaWorkExecution;
import it.spindox.stagelab.magazzino.entities.SXFatturaStatus;
import it.spindox.stagelab.magazzino.entities.StatusJobErrorType;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;



@Data
@Builder
public class DtoPaymentResponse {

    private Long id;
    private SXFatturaStatus status;
    private BigDecimal importo;
    private BigDecimal pagato;
    private OffsetDateTime dataScadenza;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private StatusJobErrorType errorType;
    private String errorMessage;
}