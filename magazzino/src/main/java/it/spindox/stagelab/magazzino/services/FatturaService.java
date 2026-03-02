
package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaSearchRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;

public interface FatturaService {

    // Ricerca paginata con filtri opzionali

    Page<FatturaResponse> search(@Valid FatturaSearchRequest request);

    // Crea una nuova fattura

    FatturaResponse create(FatturaRequest request);

    // PATCH: Aggiorna parzialmente una fattura

    FatturaResponse update(Long id, FatturaRequest request);

    // metodo paymentCheckFattura

    FatturaResponse paymentCheckFattura(Long id, BigDecimal pagatoDaAggiungere);

    // Recupera dettaglio fattura per id

    FatturaResponse getById(Long id);

    // Elenco fatture per prodotto (paginato)

    PageImpl<FatturaResponse> getByProdotto(Long idProdotto, int page, int size);

    // Cancella una fattura

    void delete(Long id);

// Metodo per verificare tutte le fatture e aggiornare lo stato se necessario

    void paymentCheckAllFatture();

    // Ricerca solo ID

    Page<Long> searchIds(FatturaSearchRequest req);

    // Recupero paginazione completo

    Page<FatturaResponse> getAllPaged(@Min(0) int page, @Min(1) int size);
}