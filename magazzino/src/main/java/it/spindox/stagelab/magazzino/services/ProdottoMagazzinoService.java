package it.spindox.stagelab.magazzino.services;

import it.spindox.stagelab.magazzino.dto.ProdottoMagazzino.ProdottoMagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.ProdottoMagazzino.ProdottoMagazzinoResponse;
import it.spindox.stagelab.magazzino.dto.ProdottoMagazzino.ProdottoMagazzinoSearchRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface ProdottoMagazzinoService {

    ProdottoMagazzinoResponse getById(Long id);

    void create(@Valid ProdottoMagazzinoRequest request);

    void update(Long id, @Valid ProdottoMagazzinoRequest request);

    <ProdottoMagazzinoSearchRequest> Page<ProdottoMagazzinoResponse> search(@Valid ProdottoMagazzinoSearchRequest request);

    Page<ProdottoMagazzinoResponse> search(@Valid ProdottoMagazzinoSearchRequest request);

    void delete(Long id);

}
