
package it.spindox.stagelab.magazzino.mappers;

import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoResponse;
import it.spindox.stagelab.magazzino.entities.Magazzino;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MagazzinoMapperImpl implements MagazzinoMapper {

    @Override
    public Magazzino toEntity(MagazzinoRequest request) {
        if (request == null) {
            return null;
        }
        Magazzino m = new Magazzino();
        m.setNome(request.getNome());
        m.setIndirizzo(request.getIndirizzo());
        // Se in create usi 'capacita' dal DTO:
        if (request.getCapacita() != null) {
            m.setCapacita(request.getCapacita());
        }
        return m;
    }

    @Override
    public MagazzinoResponse toResponse(Magazzino entity) {
        if (entity == null) {
            return null;
        }
        MagazzinoResponse r = new MagazzinoResponse();
        r.setId(entity.getId());
        r.setNome(entity.getNome());
        r.setIndirizzo(entity.getIndirizzo());
        r.setCapacita(entity.getCapacita());
        return r;
    }

    @Override
    public void updateEntity(Magazzino m, @Valid MagazzinoRequest request) {
        if (m == null || request == null) {
            return;
        }
        if (request.getNome() != null) {
            m.setNome(request.getNome());
        }
        if (request.getIndirizzo() != null) {
            m.setIndirizzo(request.getIndirizzo());
        }
        if (request.getCapacita() != null) {
            m.setCapacita(request.getCapacita());
        }
    }
}
