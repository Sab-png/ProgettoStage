
package it.spindox.stagelab.magazzino.mappers;

import it.spindox.stagelab.magazzino.dto.magazzino.*;
import it.spindox.stagelab.magazzino.entities.Magazzino;import org.springframework.stereotype.Component;


/**
 * Mapper per la conversione tra entità Magazzino e DTO
 */

public interface MagazzinoMapper {

    /**
     * Converte un DTO di creazione in una nuova entità Magazzino
     */
    Magazzino toEntity(MagazzinoCreateRequest request);

    /**
     * Converte un'entità Magazzino in un DTO di risposta
     */
    MagazzinoResponse toResponse(Magazzino entity);

    }
