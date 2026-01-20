
package it.spindox.stagelab.magazzino.dto.magazzino;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * DTO unico per le operazioni su Magazzino:
 * - CREATE: creazione di un nuovo magazzino
 * - UPDATE: aggiornamento di un magazzino esistente
 * - SEARCH: ricerca e paginazione dei magazzini
 */

@Data
public class MagazzinoRequest {

    // Campi di dominio (Create / Update / Search)


    /**
     * Nome del magazzino.
     * - CREATE: obbligatorio validazione  nel Service
     * - UPDATE: se presente viene aggiornato
     * - SEARCH: filtro opzionale
     */
    private String nome;

    /**
     * Descrizione del magazzino
     * - CREATE/UPDATE: se presente viene salvata/aggiornata
     * - SEARCH: filtro opzionale
     */
    private String descrizione;

    /**
     * Indirizzo fisico del magazzino.
     * - CREATE: obbligatorio validazione nel Service)
     * - UPDATE: se presente viene aggiornato
     * - SEARCH: filtro opzionale
     */
    private String indirizzo;

    /**
     * Capacità massima del magazzino
     * - CREATE/UPDATE: se presente deve essere > 0
     * - SEARCH: usare i campi capacitaMin/capacitaMax per range
     */
    @Positive(message = "La capacità deve essere maggiore di zero")
    private Integer capacita;


    // Campi specifici di SEARCH / opzionali


    /**
     * Filtro: capacità minima (inclusa).
     */
    @Min(value = 0, message = "La capacità minima deve essere >= 0")
    private Integer capacitaMin;

    /**
     * Filtro: capacità massima (inclusa).
     */
    @Min(value = 0, message = "La capacità massima deve essere >= 0")
    private Integer capacitaMax;

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
