
package it.spindox.stagelab.magazzino.mappers;

import it.spindox.stagelab.magazzino.dto.prodotto.ProdottoRequest;
import it.spindox.stagelab.magazzino.dto.prodotto.ProdottoResponse;
import it.spindox.stagelab.magazzino.entities.Prodotto;

/**
 * Mapper per la conversione tra entità Prodotto e DTO.
 */
public interface ProdottoMapper {

    /**
     * Converte un DTO in una nuova entità Prodotto.
     * Usato in CREATE.
     */
    Prodotto toEntity(ProdottoRequest request);

    /**
     * Converte un'entità Prodotto in un DTO di risposta.
     */
    ProdottoResponse toResponse(Prodotto entity);

    /**
     * Aggiorna un'entità Prodotto esistente
     * copia solo i campi non null dal DTO.
     * Usato in UPDATE.
     */
    void updateEntity(Prodotto prodotto, ProdottoRequest request);
}
