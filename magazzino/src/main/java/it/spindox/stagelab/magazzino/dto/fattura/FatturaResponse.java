
package it.spindox.stagelab.magazzino.dto.fattura;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO utilizzato come risposta per le API relative alla Fattura.
 * <p>
 * Contiene i dati restituiti al client e non espone mai
 * direttamente l'entità JPA.
 */
@Data
public class FatturaResponse {

    /**
     * Identificativo univoco della fattura.
     */
    private Long id;

    /**
     * Numero della fattura.
     */
    private String numero;

    /**
     * Data di emissione della fattura.
     */
    private LocalDate data;

    /**
     * ID del prodotto associato alla fattura.
     */
    private Long idProdotto;

    /**
     * Quantità del prodotto fatturato.
     */
    private Integer quantita;

    /**
     * Importo totale della fattura.
     */
    private BigDecimal importo;
}
