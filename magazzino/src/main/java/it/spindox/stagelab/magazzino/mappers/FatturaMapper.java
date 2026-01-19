
package it.spindox.stagelab.magazzino.mappers;

import it.spindox.stagelab.magazzino.dto.fattura.FatturaCreateRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaUpdateRequest;
import it.spindox.stagelab.magazzino.entities.Fattura;

/**
 * Mapper per la conversione tra entità Fattura e DTO
 */
public interface FatturaMapper {

    /**
     * Converte un DTO di creazione in una nuova entità Fattura
     */
    Fattura toEntity(FatturaCreateRequest request);

    /**
     * Converte un'entità Fattura in un DTO di risposta
     */
    FatturaResponse toResponse(Fattura entity);

    /**
     * Aggiorna un'entità Fattura esistente
     * utilizzando i dati del DTO di update
     */
    void updateEntity(Fattura fattura, FatturaUpdateRequest request);
}
