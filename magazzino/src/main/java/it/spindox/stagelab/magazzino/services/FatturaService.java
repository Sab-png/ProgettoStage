
package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaSearchRequest;
import it.spindox.stagelab.magazzino.entities.SXFatturaStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface FatturaService {

    // Ricerca paginata con filtri opzionali

    Page<FatturaResponse> search(@Valid FatturaSearchRequest request);

    // Crea una nuova fattura

    FatturaResponse create(FatturaRequest request);

    // PATCH: Aggiorna parzialmente una fattura

    FatturaResponse update(Long id, FatturaRequest request);


    // Recupera dettaglio fattura per id

    FatturaResponse getById(Long id);

    // Elenco fatture per prodotto (paginato)

    PageImpl<FatturaResponse> getByProdotto(Long idProdotto, int page, int size);

    // Cancella una fattura

    void delete(Long id);


    // Ricerca solo ID

    Page<Long> searchIds(FatturaSearchRequest req);

    // Recupero paginazione completo

    Page<FatturaResponse> getAllPaged(@Min(0) int page, @Min(1) int size);

    Page<FatturaResponse> getByStatus(SXFatturaStatus status);

    // GET by STATUS

    @Transactional(readOnly = true)
    Page<FatturaResponse> getByStatus(SXFatturaStatus status, int page, int size);
}

