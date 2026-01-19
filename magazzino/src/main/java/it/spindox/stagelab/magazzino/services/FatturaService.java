
package it.spindox.stagelab.magazzino.services;

import it.spindox.stagelab.magazzino.dto.fattura.FatturaCreateRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaSearchRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaUpdateRequest;
import org.springframework.data.domain.Page;



public interface FatturaService {
    FatturaResponse getById(Long id);

    Page<FatturaResponse> getByProdotto(Long idProdotto, int page, int size);

    void create(FatturaCreateRequest request);

    void update(Long id, FatturaUpdateRequest request);

    Page<FatturaResponse> search(FatturaSearchRequest request);
}
