
package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.magazzino.*;
import it.spindox.stagelab.magazzino.entities.Magazzino;
import jakarta.validation.Valid;import org.springframework.data.domain.Page;

public interface MagazzinoService {

    MagazzinoResponse getById(Long id);

    void create(MagazzinoRequest request);

    void update(Long id, @Valid MagazzinoRequest request);Page<MagazzinoResponse> search(@Valid MagazzinoRequest request);
void update(Long id, @Valid Magazzino request);
void delete(Long id);}
