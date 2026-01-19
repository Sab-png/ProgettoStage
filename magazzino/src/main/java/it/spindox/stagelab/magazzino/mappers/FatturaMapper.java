
package it.spindox.stagelab.magazzino.mappers;

import it.spindox.stagelab.magazzino.dto.fattura.*;
import it.spindox.stagelab.magazzino.entities.Fattura;
import org.mapstruct.Mapper;

/**
 * Mapper MapStruct per convertire tra entità Fattura e DTO.
 */
@Mapper(componentModel = "spring")
public interface FatturaMapper {

    /** Converte un DTO di creazione in una nuova entità Fattura */
    Fattura toEntity(FatturaCreateRequest request);

    /** Converte un'entità Fattura in un DTO di risposta */
    <FatturaResponse> FatturaResponse toResponse(Fattura entity);

    void updateEntity(Fattura fattura, FatturaUpdateRequest request);
}
