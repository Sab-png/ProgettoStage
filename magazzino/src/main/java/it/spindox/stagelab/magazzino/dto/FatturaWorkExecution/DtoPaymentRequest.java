package it.spindox.stagelab.magazzino.dto.FatturaWorkExecution;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import jakarta.validation.constraints.Digits;



 // Richiesta di pagamento: aumentando una Fattura esistente


@Data
public class DtoPaymentRequest {

    @NotNull(message = "L'importo non può essere nullo")
    @DecimalMin(value = "0.01", message = "Il pagamento deve essere > 0")
    @Digits(integer = 15, fraction = 2)
    private BigDecimal pagatoDaAggiungere;
}