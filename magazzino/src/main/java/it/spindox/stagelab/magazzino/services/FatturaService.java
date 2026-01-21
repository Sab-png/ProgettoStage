
package it.spindox.stagelab.magazzino.services;

import it.spindox.stagelab.magazzino.dto.fattura.FatturaRequest;
import it.spindox.stagelab.magazzino.entities.Fattura;
import org.springframework.data.domain.Page;

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
}
