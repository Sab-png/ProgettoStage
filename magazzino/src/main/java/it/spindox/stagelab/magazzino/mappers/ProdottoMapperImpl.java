
package it.spindox.stagelab.magazzino.mappers;

import it.spindox.stagelab.magazzino.dto.prodotto.ProdottoRequest;
import it.spindox.stagelab.magazzino.dto.prodotto.ProdottoResponse;
import it.spindox.stagelab.magazzino.entities.Prodotto;
import org.springframework.stereotype.Component;

/**
 * Implementazione manuale del mapper Prodotto.
 */
@Component
public class ProdottoMapperImpl implements ProdottoMapper {

    /**
     * CREATE → DTO → ENTITY
     */
    @Override
    public Prodotto toEntity(ProdottoRequest request) {
        if (request == null) {
            return null;
        }

        Prodotto prodotto = new Prodotto();
        prodotto.setNome(request.getNome());
        prodotto.setDescrizione(request.getDescrizione());
        prodotto.setPrezzo(request.getPrezzo());

        return prodotto;
    }

    /**
     * ENTITY → RESPONSE DTO
     */
    @Override
    public ProdottoResponse toResponse(Prodotto entity) {
        if (entity == null) {
            return null;
        }

        ProdottoResponse response = new ProdottoResponse();
        response.setId(entity.getId());
        response.setNome(entity.getNome());
        response.setDescrizione(entity.getDescrizione());
        response.setPrezzo(entity.getPrezzo());

        return response;
    }

    /**
     * UPDATE → copia solo i campi non null
     */
    @Override
    public void updateEntity(Prodotto prodotto, ProdottoRequest request) {
        if (prodotto == null || request == null) {
            return;
        }

        if (request.getNome() != null) {
            prodotto.setNome(request.getNome());
        }

        if (request.getDescrizione() != null) {
            prodotto.setDescrizione(request.getDescrizione());
        }

        if (request.getPrezzo() != null) {
            prodotto.setPrezzo(request.getPrezzo());
        }
    }
}
