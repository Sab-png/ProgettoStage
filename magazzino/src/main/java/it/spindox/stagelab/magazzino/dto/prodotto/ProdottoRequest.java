
package it.spindox.stagelab.magazzino.dto.prodotto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * DTO unico per le operazioni su Prodotto:
 * - CREATE: creazione di un nuovo prodotto
 * - UPDATE: aggiornamento  di un prodotto esistente
 * - SEARCH: ricerca e paginazione dei prodotti
 */

@Data
public class ProdottoRequest {


    // Campi di dominio (Create / Update / Search)


    /**
     * Nome del prodotto.
     * - CREATE: obbligatorio (validazione nel Service)
     * - UPDATE: se presente viene aggiornato
     * - SEARCH: filtro opzionale
     */
    private String nome;

    /**
     * Descrizione del prodotto.
     * - CREATE/UPDATE: se presente viene salvata/aggiornata
     * - SEARCH: filtro opzionale
     */
    private String descrizione;

    /**
     * Prezzo del prodotto.
     * - CREATE/UPDATE: se presente deve essere > 0
     * - SEARCH: preferire i campi prezzoMin/prezzoMax per range
     * <p>
     * Nota: per valori monetari è preferibile BigDecimal, ma qui
     * si mantiene Double per coerenza con le tue classi esistenti.
     */
    @Positive(message = "Il prezzo deve essere maggiore di zero")
    private Double prezzo;


    // Campi specifici di SEARCH/ opzionali

    /**
     * Filtro: prezzo minimo (incluso).
     */
    private Double prezzoMin;

    /**
     * Filtro: prezzo massimo (incluso).
     */
    private Double prezzoMax;

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

    public String getCodice() {
        return "";
    }

    public String getCategoria() {
        return "";
    }
}
