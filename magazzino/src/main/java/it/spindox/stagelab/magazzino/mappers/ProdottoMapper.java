package it.spindox.stagelab.magazzino.mappers;

import it.spindox.stagelab.magazzino.dto.response.ProdottoResponse;
import it.spindox.stagelab.magazzino.entities.Prodotto;

public class ProdottoMapper {

    public static ProdottoResponse toResponse(Prodotto prodotto) {
        ProdottoResponse response = new ProdottoResponse();
        response.setId(prodotto.getId());
        response.setNome(prodotto.getNome());
        response.setDescrizione(prodotto.getDescrizione());
        response.setPrezzo(prodotto.getPrezzo());
        return response;
    }
}

