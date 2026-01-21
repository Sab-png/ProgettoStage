
package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;
import it.spindox.stagelab.magazzino.entities.Fattura;
import it.spindox.stagelab.magazzino.entities.Prodotto;
import it.spindox.stagelab.magazzino.repositories.ProdottoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Implementazione FatturaMapper.
 */

@Component
@RequiredArgsConstructor
public class FatturaMapperImpl implements FatturaMapper {

    private final ProdottoRepository prodottoRepository;

    @Override
    public Fattura toEntity(FatturaRequest request) {
        if (request == null) return null;

        Fattura fattura = new Fattura();
        fattura.setDataFattura(request.getData());

        if (request.getImporto() != null) {
            fattura.setImporto(BigDecimal.valueOf(request.getImporto().doubleValue()));
        }

        if (request.getIdProdotto() != null) {
            Prodotto prodotto =
                    prodottoRepository.getReferenceById(request.getIdProdotto());
            fattura.setProdotto(prodotto);
        }

        return fattura;
    }

    @Override
    public FatturaResponse toResponse(Fattura entity) {
        if (entity == null) return null;

        FatturaResponse response = new FatturaResponse();
        response.setId(entity.getId());
        response.setData(entity.getDataFattura());

        if (entity.getImporto() != null) {
            response.setImporto(BigDecimal.valueOf(entity.getImporto()));
        }

        response.setIdProdotto(
                entity.getProdotto() != null ? entity.getProdotto().getId() : null
        );

        return response;
    }

    @Override
    public void updateEntity(Fattura target, FatturaRequest request) {
        if (request.getData() != null) {
            target.setDataFattura(request.getData());
        }

        if (request.getImporto() != null) {
            target.setImporto(BigDecimal.valueOf(request.getImporto().doubleValue()));
        }

        if (request.getIdProdotto() != null) {
            Prodotto prodotto =
                    prodottoRepository.getReferenceById(request.getIdProdotto());
            target.setProdotto(prodotto);
        }
    }
}
