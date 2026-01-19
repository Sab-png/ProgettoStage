
package it.spindox.stagelab.magazzino.dto.fattura;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO utilizzato per la creazione di una nuova Fattura.
 * <p>
 * Viene validato automaticamente dal controller tramite @Valid.
 */
@Data
public class FatturaCreateRequest {

    /**
     * Numero della fattura.
     * Campo obbligatorio.
     */
    @NotBlank
    private String numero;

    /**
     * Data di emissione della fattura.
     * Campo obbligatorio.
     */
    @NotNull
    private LocalDate data;

    /**
     * ID del prodotto associato alla fattura.
     * Campo obbligatorio.
     */
    @NotNull
    private Long idProdotto;

    /**
     * Quantità del prodotto fatturato.
     * Deve essere positiva.
     */
    @NotNull
    @Positive
    private Integer quantita;

    /**
     * Importo totale della fattura.
     * Deve essere maggiore di zero.
     */
    @NotNull
    @Positive
    private BigDecimal importo;
}
