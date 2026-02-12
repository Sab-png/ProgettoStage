package it.spindox.stagelab.magazzino.services;

import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface MagazzinoService {

    MagazzinoResponse getById(Long id);

    void create(@Valid MagazzinoRequest request);

    void update(Long id, @Valid MagazzinoRequest request);

    Page<MagazzinoResponse> search(@Valid MagazzinoRequest request);

    void delete(Long id);

    void checkStockLevels();

    Page<Long> searchIds(MagazzinoRequest req);
}
