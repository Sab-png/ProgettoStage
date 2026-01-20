
package it.spindox.stagelab.magazzino.mappers;
import org.springframework.stereotype.Component;
import it.spindox.stagelab.magazzino.dto.prodotto.ProdottoCreateRequest;
import it.spindox.stagelab.magazzino.dto.prodotto.ProdottoResponse;
import it.spindox.stagelab.magazzino.dto.prodotto.ProdottoUpdateRequest;
import it.spindox.stagelab.magazzino.entities.Prodotto;

@Component
public class ProdottoMapperImpl implements ProdottoMapper {

    @Override
    public Prodotto toEntity(ProdottoCreateRequest request) {
        if (request == null) return null;

        Prodotto p = new Prodotto();

        return p;
    }

    @Override
    public ProdottoResponse toResponse(Prodotto entity) {
        if (entity == null) return null;

        ProdottoResponse r = new ProdottoResponse();

        return r;
    }

    @Override
    public void updateEntity(Prodotto prodotto, ProdottoUpdateRequest request) {
        if (prodotto == null || request == null) return;


    }
}
