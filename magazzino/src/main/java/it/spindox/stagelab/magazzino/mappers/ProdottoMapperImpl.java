
package it.spindox.stagelab.magazzino.mappers;

import it.spindox.stagelab.magazzino.dto.prodotto.ProdottoRequest;
import it.spindox.stagelab.magazzino.dto.prodotto.ProdottoResponse;
import it.spindox.stagelab.magazzino.entities.Prodotto;
import org.springframework.stereotype.Component;

@Component
public class ProdottoMapperImpl implements ProdottoMapper {

    @Override
    public Prodotto toEntity(ProdottoRequest request) {
        if (request == null) {
            return null;
        }

        Prodotto p = new Prodotto();
        p.setNome(request.getNome());
        p.setDescrizione(request.getDescrizione());

        // request.getPrezzo() è già BigDecimal → assegnazione diretta
        if (request.getPrezzo() != null) {
            p.setPrezzo(request.getPrezzo());
        }

        return p;
    }

    @Override
    public ProdottoResponse toResponse(Prodotto entity) {
        if (entity == null) {
            return null;
        }

        ProdottoResponse r = new ProdottoResponse();
        r.setId(entity.getId());
        r.setNome(entity.getNome());
        r.setDescrizione(entity.getDescrizione());
        r.setPrezzo(entity.getPrezzo()); // BigDecimal → pass-through

        return r;
    }

    @Override
    public void updateEntity(Prodotto p, ProdottoRequest request) {
        if (p == null || request == null) {
            return;
        }

        if (request.getNome() != null) {
            p.setNome(request.getNome());
        }
        if (request.getDescrizione() != null) {
            p.setDescrizione(request.getDescrizione());
        }
        if (request.getPrezzo() != null) {
            p.setPrezzo(request.getPrezzo()); // BigDecimal → assegnazione diretta
        }
    }
}
