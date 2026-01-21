
package it.spindox.stagelab.magazzino.dto.fattura;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO utilizzato come risposta per le API relative alla Fattura.
 * Contiene i dati restituiti al client e non espone mai
 * direttamente l'entità JPA.
 */

@Data
public class FatturaResponse {

    private Long id;
    private String numero;
    private LocalDate dataFattura;
    private BigDecimal importo;
    private Integer quantita;
    private Long idProdotto;
}
