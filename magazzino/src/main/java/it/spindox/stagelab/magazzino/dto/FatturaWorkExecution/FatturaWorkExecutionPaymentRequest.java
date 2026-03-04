package it.spindox.stagelab.magazzino.dto.FatturaWorkExecution;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;



@Data
public class FatturaWorkExecutionPaymentRequest {
    @NotNull
    @DecimalMin(value = "0.01", message = "Il pagamento deve essere > 0")
    private BigDecimal pagatoDaAggiungere;
}