
package it.spindox.stagelab.magazzino.dto.fattura;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO unico per la gestione delle operazioni su Fattura.
 */

@Data
public class FatturaRequest {

    // Campi di dominio (Create/Update/Search)


    /**
     * Numero della fattura.
     * - CREATE: obbligatorio (validazione nel service)
     * - UPDATE: opzionale
     * - SEARCH: filtro opzionale
     */
    private String numero;

    /**
     * Data di emissione della fattura.
     * - CREATE: obbligatoria (validazione nel service)
     * - UPDATE: opzionale
     * - SEARCH: possibile filtro
     */
    private LocalDate data;

    /**
     * Identificativo del prodotto associato alla fattura.
     * - CREATE: obbligatorio (validazione  nel Service)
     * - UPDATE: solitamente non modificabile
     * - SEARCH: filtro opzionale
     */
    private Long idProdotto;

    /**
     * Quantità del prodotto fatturato.
     * - CREATE/UPDATE: se presente deve essere > 0
     * - SEARCH:  opzionale
     */
    @Positive(message = "La quantità deve essere maggiore di zero")
    private Integer quantita;

    /**
     * Importo totale della fattura.
     * - CREATE/UPDATE: se presente deve essere > 0
     * - SEARCH: usare importoMin/importoMax per range
     *
     * Nota: usare BigDecimal per valori monetari è preferibile a Double.
     */
    @Positive(message = "L'importo deve essere maggiore di zero")
    private BigDecimal importo;


    // Campi specifici di SEARCH (tutti opzionali)

    /**
     * Filtro: data minima (inclusa).
     */
    private LocalDate dataDa;

    /**
     * Filtro: data massima (inclusa).
     */
    private LocalDate dataA;

    /**
     * Filtro: importo minimo (incluso).
     */
    private BigDecimal importoMin;

    /**
     * Filtro: importo massimo (incluso).
     */
    private BigDecimal importoMax;

    /**
     * Paginazione: numero di pagina (>= 0).
     */
    @Min(value = 0, message = "La pagina deve essere >= 0")
    private int page = 0;

    /**
     * Paginazione: dimensione pagina (>= 1).
     */
    @Min(value = 1, message = "La dimensione deve essere >= 1")
    private int size = 10;

}
