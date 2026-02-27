package it.spindox.stagelab.magazzino.dto.fattura;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;



@Data
@NoArgsConstructor
@AllArgsConstructor

public class FatturaPagamentoRequest {
    @NotNull(message = "L'importo pagato è obbligatorio")
    @Positive(message = "Il pagamento deve essere positivo")
    private BigDecimal pagatoDaAggiungere;
}