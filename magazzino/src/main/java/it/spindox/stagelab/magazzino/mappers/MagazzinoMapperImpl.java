
package it.spindox.stagelab.magazzino.mappers;
import org.springframework.stereotype.Component;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoCreateRequest;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoResponse;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoUpdateRequest;
import it.spindox.stagelab.magazzino.entities.Magazzino;

@Component
public class MagazzinoMapperImpl implements MagazzinoMapper {

    @Override
    public Magazzino toEntity(MagazzinoCreateRequest request) {
        if (request == null) return null;

        Magazzino magazzino = new Magazzino();
        magazzino.setNome(request.getNome());
        magazzino.setIndirizzo(request.getDescrizione());
        magazzino.setCapacita(request.getCapacita());

        return magazzino;
    }

    @Override
    public MagazzinoResponse toResponse(Magazzino entity) {
        if (entity == null) return null;

        MagazzinoResponse response = new MagazzinoResponse();
        response.setId(entity.getId());
        response.setNome(entity.getNome());
        response.setIndirizzo(entity.getDescrizione());
        response.setCapacita(entity.getCapacita());

        return response;
    }

    @Override
    public void updateEntity(Magazzino magazzino, MagazzinoUpdateRequest request) {
        if (magazzino == null || request == null) return;

        if (request.getNome() != null) {
            magazzino.setNome(request.getNome());
        }
        if (request.getDescrizione() != null) {
            magazzino.setDescrizione(request.getDescrizione());
        }
        if (request.getCapacita() != null) {
            magazzino.setCapacita(request.getCapacita());
        }
    }
}
