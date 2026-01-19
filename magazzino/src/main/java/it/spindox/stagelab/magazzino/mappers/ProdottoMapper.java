package it.spindox.stagelab.magazzino.mappers;

import it.spindox.stagelab.magazzino.dto.prodotto.*;
import it.spindox.stagelab.magazzino.entities.Prodotto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Mapper MapStruct per convertire tra entità Prodotto e DTO.
 * <p>
 * - Evita di esporre direttamente le entità JPA nelle API.
 * - Gestisce conversioni automatiche tra DTO e entity.
 * - updateEntity permette aggiornamenti parziali (PATCH).
 */
@Mapper(componentModel = "spring")
public interface ProdottoMapper {

    /** Converte un DTO di creazione in una nuova entità Prodotto */
    Prodotto toEntity(ProdottoCreateRequest request);

    /** Converte un'entità Prodotto in un DTO di risposta */
    ProdottoResponse toResponse(Prodotto entity);

    /** Aggiorna un'entità esistente con i campi presenti nel DTO di update */
    void updateEntity(@MappingTarget Prodotto entity, ProdottoUpdateRequest request);
}
