
package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoResponse;
import it.spindox.stagelab.magazzino.entities.Magazzino;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

/**
 * Implementazione manuale del mapper Magazzino.
 */
@Component
public class MagazzinoMapperImpl implements MagazzinoMapper {

    /**
     * CREATE → DTO → ENTITY
     */
    @Override
    public Magazzino toEntity(MagazzinoRequest request) {
        if (request == null) {
            return null;
        }

        Magazzino magazzino = new Magazzino();
        magazzino.setNome(request.getNome());
        magazzino.setDescrizione(request.getDescrizione());
        magazzino.setIndirizzo(request.getIndirizzo());
        magazzino.setCapacita(request.getCapacita());

        return magazzino;
    }

    /**
     * ENTITY → RESPONSE DTO
     */
    @Override
    public MagazzinoResponse toResponse(Magazzino entity) {
        if (entity == null) {
            return null;
        }

        MagazzinoResponse response = new MagazzinoResponse();
        response.setId(entity.getId());
        response.setNome(entity.getNome());
        response.setDescrizione(entity.getDescrizione());
        response.setIndirizzo(entity.getIndirizzo());
        response.setCapacita(entity.getCapacita());

        return response;
    }

    /**
     * UPDATE → copia solo i campi non null dal DTO all'entity
     */
    @Override
    public void updateEntity(Magazzino magazzino, @Valid Magazzino request) {
        if (magazzino == null || request == null) {
            return;
        }

        if (request.getNome() != null) {
            magazzino.setNome(request.getNome());
        }

        if (request.getDescrizione() != null) {
            magazzino.setDescrizione(request.getDescrizione());
        }

        if (request.getIndirizzo() != null) {
            magazzino.setIndirizzo(request.getIndirizzo());
        }

        if (request.getCapacita() != null) {
            magazzino.setCapacita(request.getCapacita());
        }
    }
}
