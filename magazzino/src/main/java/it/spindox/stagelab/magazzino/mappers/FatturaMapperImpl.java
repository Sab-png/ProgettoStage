
package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;
import it.spindox.stagelab.magazzino.entities.Fattura;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Implementazione FatturaMapper.
 */
@Component
public class FatturaMapperImpl implements FatturaMapper {

    /**
     * CREATE → DTO → ENTITY
     */
    @Override
    public Fattura toEntity(FatturaRequest request) {
        if (request == null) {
            return null;
        }

        Fattura fattura = new Fattura();
        fattura.setNumero(request.getNumero());
        fattura.setData(request.getData());
        fattura.setImporto(request.getImporto());
        fattura.setQuantita(request.getQuantita());
        fattura.setIdProdotto(request.getIdProdotto());

        return fattura;
    }

    /**
     * ENTITY → RESPONSE DTO
     */
    @Override
    public FatturaResponse toResponse(Fattura entity) {
        if (entity == null) {
            return null;
        }

        FatturaResponse response = new FatturaResponse();
        response.setId(entity.getId());
        response.setNumero(entity.getNumero());
        response.setData(entity.getData());
        response.setImporto(BigDecimal.valueOf(entity.getImporto()));
        response.setQuantita(entity.getQuantita());
        response.setIdProdotto(entity.getIdProdotto());

        return response;
    }

    /**
     * UPDATE → copia solo i campi non null dal DTO all'entity
     */
    @Override
    public void updateEntity(Fattura target, FatturaRequest request) {
        if (target == null || request == null) {
            return;
        }

        if (request.getNumero() != null) {
            target.setNumero(request.getNumero());
        }

        if (request.getData() != null) {
            target.setData(request.getData());
        }

        if (request.getImporto() != null) {
            target.setImporto(request.getImporto());
        }

        if (request.getQuantita() != null) {
            target.setQuantita(request.getQuantita());
        }

        if (request.getIdProdotto() != null) {
            target.setIdProdotto(request.getIdProdotto());
        }
    }
}
