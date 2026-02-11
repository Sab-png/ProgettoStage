
package it.spindox.stagelab.magazzino.dto.fattura;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor


public class FatturaRequest {

    @NotNull(message = "La data fattura è obbligatoria")
    private LocalDate dataFattura;

    @NotNull(message = "Il prodotto è obbligatorio")
    private Long idProdotto;

    @NotNull(message = "La quantità è obbligatoria")
    @Positive(message = "La quantità deve essere maggiore di zero")
    private Integer quantita;

    @NotNull(message = "L'importo è obbligatorio")
    @Positive(message = "L'importo deve essere maggiore di zero")
    private BigDecimal importo;

    public int getPage() {
        return 0;
    }

    public int getSize() {
        return 0;
    }

    public Object getImportoMin() {
        return null;
    }

    public Object getImportoMax() {
        return null;
    }

    public String getNumero() {
        return "";
    }

    public LocalDate getDataFrom() {
        return null;
    }

    public LocalDate getDataTo() {
        return null;
    }
}