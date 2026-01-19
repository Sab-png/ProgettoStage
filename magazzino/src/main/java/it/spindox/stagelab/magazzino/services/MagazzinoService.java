
package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.magazzino.*;
import org.springframework.data.domain.Page;

public interface MagazzinoService {

    MagazzinoResponse getById(Long id);

    void create(MagazzinoCreateRequest request);

    Page<MagazzinoResponse> search(MagazzinoSearchRequest request);
}
