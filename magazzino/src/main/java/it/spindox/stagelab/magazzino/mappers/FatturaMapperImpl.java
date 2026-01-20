
package it.spindox.stagelab.magazzino.mappers;
import org.springframework.stereotype.Component;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaCreateRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaUpdateRequest;
import it.spindox.stagelab.magazzino.entities.Fattura;

@Component
public class FatturaMapperImpl implements FatturaMapper {

    @Override
    public Fattura toEntity(FatturaCreateRequest request) {
        if (request == null) return null;

        Fattura f = new Fattura();
        // es:
        // f.setNumero(request.getNumero());
        // f.setData(request.getData());
        // f.setImporto(request.getImporto());
        return f;
    }

    @Override
    public FatturaResponse toResponse(Fattura entity) {
        if (entity == null) return null;

        FatturaResponse r = new FatturaResponse();
        // es:
        // r.setId(entity.getId());
        // r.setNumero(entity.getNumero());
        // r.setImporto(entity.getImporto());
        return r;
    }

    @Override
    public void updateEntity(Fattura target, FatturaUpdateRequest request) {
        if (target == null || request == null) return;

        // es:
        // if (request.getNumero() != null)
        //     target.setNumero(request.getNumero());
    }
}
