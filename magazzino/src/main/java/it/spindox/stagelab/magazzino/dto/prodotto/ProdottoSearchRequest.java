package it.spindox.stagelab.magazzino.dto.prodotto;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdottoSearchRequest {

    // Filtri opzionali

    private String nome;
    private String descrizione;
    private BigDecimal prezzoMin;
    private BigDecimal prezzoMax;

    // Paginazione
    @Min(0)
    private Integer page = 0;

    @Min(1)
    private Integer size = 10;
}