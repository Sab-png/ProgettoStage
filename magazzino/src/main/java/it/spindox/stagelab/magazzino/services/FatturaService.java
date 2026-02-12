
package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaSearchRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public interface FatturaService {

    /**
     * Ricerca paginata con filtri opzionali (usando i campi del DTO request).
     */
    Page<FatturaResponse> search(@Valid FatturaSearchRequest request);

    /**
     * Crea una nuova fattura.
     */
    FatturaResponse create(FatturaRequest request);

    /**
     * Aggiorna parzialmente una fattura (patch).
     */
    FatturaResponse update(Long id, FatturaRequest request) throws Throwable;

    /**
     * Dettaglio fattura per id.
     */
    FatturaResponse getById(Long id) throws Throwable;

    /**
     * Elenco fatture per prodotto (paginato).
     * Il sort è opzionale, es. "dataFattura,desc"
     */
    PageImpl<FatturaResponse> getByProdotto(Long idProdotto, int page, int size);

    /**
     * Cancella una fattura.
     */
    void delete(Long id);

    Page<Long> searchIds(FatturaSearchRequest req);

    Page<FatturaResponse> getAllPaged(@Min(0) int page, @Min(1) int size);
}
