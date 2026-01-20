
package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface FatturaService {

    // READ (query)
    FatturaResponse getById(Long id);

    Page<FatturaResponse> getByProdotto(Long idProdotto, int page, int size);

    Page<FatturaResponse> search(@Valid FatturaRequest request);

    // WRITE (command)
    FatturaResponse create(@Valid FatturaRequest request);

    FatturaResponse update(Long id, @Valid FatturaRequest request);

    void delete(Long id);
}
