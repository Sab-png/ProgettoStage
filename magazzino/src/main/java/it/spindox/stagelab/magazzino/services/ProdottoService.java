
package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.prodotto.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface ProdottoService {

    ProdottoResponse getById(Long id);

    void create(ProdottoRequest request);

    void update(Long id, @Valid ProdottoRequest request);

    Page<ProdottoResponse> search(@Valid ProdottoRequest request);
void delete(Long id);}
