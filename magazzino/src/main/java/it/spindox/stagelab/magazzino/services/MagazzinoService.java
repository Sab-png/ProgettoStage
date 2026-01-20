
package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.controllers.MagazzinoUpdateRequest;import it.spindox.stagelab.magazzino.dto.magazzino.*;
import jakarta.validation.Valid;import org.springframework.data.domain.Page;

public interface MagazzinoService {

    MagazzinoResponse getById(Long id);

    void create(MagazzinoCreateRequest request);

    Page<MagazzinoResponse> search(MagazzinoSearchRequest request);
void update(Long id, @Valid MagazzinoUpdateRequest request); void delete(Long id);}
