
package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;import it.spindox.stagelab.magazzino.entities.Fattura;
import jakarta.validation.constraints.Min;import org.springframework.data.domain.Page;import org.springframework.data.domain.Range;

public interface FatturaService {

    /**
     * Ricerca fatture con filtri opzionali + paginazione
     */
    Page<Fattura> search(FatturaRequest request);

    /**
     * Creazione fattura
     */
    Fattura create(FatturaRequest request);

    /**
     * Aggiornamento fattura
     */
    Fattura update(Long id, FatturaRequest request);

    /**
     * Dettaglio fattura
     */
    Fattura findById(Long id);
FatturaResponse getById(Long id); Range getByProdotto(Long idProdotto, @Min(0) int page, @Min(1) int size); void delete(Long id);}
