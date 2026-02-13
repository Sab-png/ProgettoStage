
package it.spindox.stagelab.magazzino.dto.prodotto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProdottoResponse {

    private Long id;
    private String nome;
    private String descrizione;
    private BigDecimal prezzo;
}

