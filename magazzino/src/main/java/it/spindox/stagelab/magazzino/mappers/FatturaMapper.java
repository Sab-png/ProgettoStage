
package it.spindox.stagelab.magazzino.mappers;

import it.spindox.stagelab.magazzino.dto.fattura.FatturaCreateRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaUpdateRequest;
import it.spindox.stagelab.magazzino.entities.Fattura;
import org.springframework.stereotype.Component;

/**
 * Mapper per la conversione tra entità Fattura e DTO
 * L'implementazione concreta è FatturaMapperImpl.
 */

public interface FatturaMapper {

    Fattura toEntity(FatturaCreateRequest request);

    FatturaResponse toResponse(Fattura entity);

    void updateEntity(Fattura target, FatturaUpdateRequest request);
}
