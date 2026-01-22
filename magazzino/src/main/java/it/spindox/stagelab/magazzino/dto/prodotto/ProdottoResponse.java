
package it.spindox.stagelab.magazzino.dto.prodotto;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProdottoResponse {

    private Long id;
    private String nome;
    private String descrizione;
    private BigDecimal prezzo;
}

