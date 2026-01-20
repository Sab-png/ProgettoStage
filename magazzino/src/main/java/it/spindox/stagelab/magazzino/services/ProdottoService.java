
package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.prodotto.*;
import org.springframework.data.domain.Page;

public interface ProdottoService {

    ProdottoResponse getById(Long id);

    void create(ProdottoRequest request);

    void update(Long id, ProdottoUpdateRequest request);

    Page<ProdottoResponse> search(ProdottoSearchRequest request);
void delete(Long id);}
