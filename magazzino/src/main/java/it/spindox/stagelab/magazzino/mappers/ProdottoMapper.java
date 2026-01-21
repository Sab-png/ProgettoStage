
package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.prodotto.ProdottoRequest;
import it.spindox.stagelab.magazzino.dto.prodotto.ProdottoResponse;
import it.spindox.stagelab.magazzino.entities.Prodotto;

public interface ProdottoMapper {

    Prodotto toEntity(ProdottoRequest request);

    ProdottoResponse toResponse(Prodotto entity);

    void updateEntity(Prodotto prodotto, ProdottoRequest request);
}
