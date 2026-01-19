
package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.fattura.*;
import it.spindox.stagelab.magazzino.entities.Fattura;
import it.spindox.stagelab.magazzino.exceptions.ResourceNotFoundException;
import it.spindox.stagelab.magazzino.mappers.FatturaMapper;
import it.spindox.stagelab.magazzino.repositories.FatturaRepository;
import it.spindox.stagelab.magazzino.services.FatturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Implementazione del service Fattura.
 */
@Service
@RequiredArgsConstructor
public class FatturaServiceImpl implements FatturaService {

    private final FatturaRepository repository;
    private final FatturaMapper mapper;

    @Override
    public FatturaResponse getById(Long id) {
        Fattura fattura = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fattura non trovata"));
        return mapper.toResponse(fattura);
    }

    @Override
    public Page getByProdotto(Long idProdotto, int page, int size) {
        return null;
    }

    @Override
    public void create(FatturaCreateRequest request) {
        repository.save(mapper.toEntity(request));
    }

    @Override
    public void update(Long id, FatturaUpdateRequest request) {
        Fattura fattura = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fattura non trovata"));
        mapper.updateEntity(fattura, request);
        repository.save(fattura);
    }

    @Override
    public Page<FatturaResponse> search(FatturaSearchRequest request) {
        return null; // da implementare
    }
}
