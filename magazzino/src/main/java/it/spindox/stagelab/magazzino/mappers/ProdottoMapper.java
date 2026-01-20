
package it.spindox.stagelab.magazzino.mappers;

import it.spindox.stagelab.magazzino.dto.prodotto.*;
import it.spindox.stagelab.magazzino.entities.Prodotto;


/**
 * Mapper per la conversione tra entità Prodotto e DTO
 */

public interface ProdottoMapper {

    /**
     * Converte un DTO di creazione in una nuova entità Prodotto
     */
    Prodotto toEntity(ProdottoRequest request);

    /**
     * Converte un'entità Prodotto in un DTO di risposta
     */
    ProdottoResponse toResponse(Prodotto entity);

    /**
     * Aggiorna un'entità Prodotto esistente
     * utilizzando i dati del DTO di update
     */
    void updateEntity(Prodotto prodotto, ProdottoUpdateRequest request);
}
