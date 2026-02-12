
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

    /**
     * Converte un DTO MagazzinoRequest in una entity Magazzino.
     *
     * Viene usato in:
     *  - create()
     *
     * NOTE:
     *  - L'ID è generato dal DB
     *  - 'capacita' è facoltativa nel DTO: viene mappata solo se presente (!= null)
     */
    @Override
    public Magazzino toEntity(MagazzinoRequest request) {
        if (request == null) {
            return null;
        }
        Magazzino m = new Magazzino();
        m.setNome(request.getNome());
        m.setIndirizzo(request.getIndirizzo());

        // Mappa 'capacita' se fornita nella request
        if (request.getCapacita() != null) {
            m.setCapacita(request.getCapacita());
        }
        return m;
    }

    /**
     * Converte una entity Magazzino in un DTO MagazzinoResponse.
     *
     * Viene usato quando si restituisce il magazzino al client:
     *  - GET /magazzini/{id}
     *  - GET /magazzini/list
     *  - POST /magazzini/search
     *
     * NOTE:
     *  - Protegge da null su entity
     */
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

    /**
     * Aggiorna una entity esistente in modalità "PATCH":
     * modifica solo i campi NON NULL presenti nella request.
     *
     * Viene usato in:
     *  - update()
     *
     * NOTE:
     *  - Se request è null non viene effettuata alcuna modifica
     */
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