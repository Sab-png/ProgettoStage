package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.prodottomagazzino.ProdottoMagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.prodottomagazzino.ProdottoMagazzinoResponse;
import it.spindox.stagelab.magazzino.dto.prodottomagazzino.ProdottoMagazzinoSearchRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;


public interface ProdottoMagazzinoService {

    Object getById(Long id);

    void create(@Valid ProdottoMagazzinoRequest request);

    void update(Long id, @Valid ProdottoMagazzinoRequest request);

    void delete(Long id);

    @Transactional(readOnly = true)
    Object getAllPaged(int page, int size);

    Page<Long> searchIds(ProdottoMagazzinoSearchRequest request);

    Page<ProdottoMagazzinoResponse> search(ProdottoMagazzinoSearchRequest request);
}