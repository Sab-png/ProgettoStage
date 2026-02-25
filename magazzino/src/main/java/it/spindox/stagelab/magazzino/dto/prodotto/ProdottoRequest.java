
package it.spindox.stagelab.magazzino.dto.prodotto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;



@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProdottoRequest {

    private String nome;
    private String descrizione;

    @Positive(message = "Il prezzo deve essere maggiore di zero")
    private BigDecimal prezzo;

    private BigDecimal prezzoMin;
    private BigDecimal prezzoMax;

    @Min(0)
    private Integer page;
    @Min(1)
    private Integer size;

    private Integer quantita;
    private Integer scortaMinima;

}