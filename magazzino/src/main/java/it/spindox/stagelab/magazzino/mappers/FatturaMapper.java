
package it.spindox.stagelab.magazzino.mappers;

import it.spindox.stagelab.magazzino.dto.fattura.FatturaRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;
import it.spindox.stagelab.magazzino.entities.Fattura;

/**
 * Mapper per la conversione tra DTO e Entity di Fattura.
 */
public interface FatturaMapper {

    /**
     * Converte una request DTO in una nuova entity Fattura.
     * Usato principalmente in CREATE.
     */
    Fattura toEntity(FatturaRequest request);

    /**
     * Converte una entity Fattura in response DTO.
     */
    FatturaResponse toResponse(Fattura entity);

    /**
     * Aggiorna una entity Fattura esistente usando i campi non null del DTO.
     * Usato in UPDATE.
     */
    void updateEntity(Fattura target, FatturaRequest request);
}
