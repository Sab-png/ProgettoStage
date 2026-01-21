
package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.prodotto.ProdottoRequest;
import it.spindox.stagelab.magazzino.dto.prodotto.ProdottoResponse;
import it.spindox.stagelab.magazzino.entities.Prodotto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProdottoMapperImpl implements ProdottoMapper {

    @Override
    public Prodotto toEntity(ProdottoRequest request) {
        Prodotto p = new Prodotto();
        p.setNome(request.getNome());
        p.setDescrizione(request.getDescrizione());
        p.setPrezzo(BigDecimal.valueOf(request.getPrezzo()));
        return p;
    }

    @Override
    public ProdottoResponse toResponse(Prodotto entity) {
        ProdottoResponse r = new ProdottoResponse();
        r.setId(entity.getId());
        r.setNome(entity.getNome());
        r.setDescrizione(entity.getDescrizione());
        r.setPrezzo(entity.getPrezzo());
        return r;
    }

    @Override
    public void updateEntity(Prodotto p, ProdottoRequest request) {
        if (request.getNome() != null) p.setNome(request.getNome());
        if (request.getDescrizione() != null) p.setDescrizione(request.getDescrizione());
        if (request.getPrezzo() != null) p.setPrezzo(request.getPrezzo());
    }
}
