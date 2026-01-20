package it.spindox.stagelab.magazzino.dto.fattura;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.time.LocalDate;

@Data
public class FatturaUpdateRequest {

    // Nuova data della fattura (opzionale)
    private LocalDate dataFattura;

    // Nuovo importo – se presente deve essere > 0
    @Positive(message = "L'importo deve essere maggiore di zero")
    private Double importo;

    public <__TMP__> __TMP__ getNumero() {
        return null;
    }

    public @NotNull LocalDate getData() {
        return null;
    }
}
