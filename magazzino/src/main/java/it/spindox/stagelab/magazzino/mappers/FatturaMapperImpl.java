
package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;
import it.spindox.stagelab.magazzino.entities.Fattura;
import it.spindox.stagelab.magazzino.entities.Prodotto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FatturaMapperImpl implements FatturaMapper {

    @Override
    public Fattura toEntity(FatturaRequest request, Prodotto prodotto) {
        Fattura f = new Fattura();
        f.setNumero(request.getNumero());
        f.setDataFattura(request.getDataFattura());
        f.setImporto(BigDecimal.valueOf(request.getImporto()));
        f.setQuantita(request.getQuantita());
        f.setProdotto(prodotto);
        return f;
    }

    @Override
    public FatturaResponse toResponse(Fattura entity) {
        FatturaResponse r = new FatturaResponse();
        r.setId(entity.getId());
        r.setNumero(entity.getNumero());
        r.setDataFattura(entity.getDataFattura());
        r.setImporto(entity.getImporto());
        r.setQuantita(entity.getQuantita());

        if (entity.getProdotto() != null) {
            r.setIdProdotto(entity.getProdotto().getId());
        }
        return r;
    }

    @Override
    public void updateEntity(Fattura target, FatturaRequest request, Prodotto prodotto) {

        if (request.getNumero() != null) {
            target.setNumero(request.getNumero());
        }
        if (request.getDataFattura() != null) {
            target.setDataFattura(request.getDataFattura());
        }
        if (request.getImporto() != null) {
            target.setImporto(BigDecimal.valueOf(request.getImporto()));
        }
        if (request.getQuantita() != null) {
            target.setQuantita(request.getQuantita());
        }
        if (prodotto != null) {
            target.setProdotto(prodotto);
        }
    }
}
