
package it.spindox.stagelab.magazzino.services;

import it.spindox.stagelab.magazzino.dto.fattura.FatturaRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;
import org.springframework.data.domain.PageImpl;

public interface FatturaService {

    /**
     * Ricerca paginata con filtri opzionali (usando i campi del DTO request).
     */
    Object search(FatturaRequest request);

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
}
