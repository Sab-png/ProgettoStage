package it.spindox.stagelab.magazzino.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.time.LocalDate;

@Data
public class FatturaRequest {

    @NotNull(message = "Data fattura obbligatoria")
    private LocalDate dataFattura;

    @NotNull(message = "Importo obbligatorio")
    @Positive(message = "Importo deve essere positivo")
    private Double importo;

    @NotNull(message = "Id prodotto obbligatorio")
    private Long prodottoId;
}
