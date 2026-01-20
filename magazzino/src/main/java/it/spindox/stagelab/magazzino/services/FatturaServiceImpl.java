
package it.spindox.stagelab.magazzino.services;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.*;
import it.spindox.stagelab.magazzino.mappers.FatturaMapper;
import it.spindox.stagelab.magazzino.repositories.FatturaRepository;
import it.spindox.stagelab.magazzino.services.FatturaService;
import it.spindox.stagelab.magazzino.dto.fattura.*;
import it.spindox.stagelab.magazzino.entities.Fattura;

@Service
@RequiredArgsConstructor
public class FatturaServiceImpl implements FatturaService {

    private final FatturaRepository repository;
    private final FatturaMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public FatturaResponse getById(Long id) {
        Fattura fattura = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fattura non trovata"));
        return mapper.toResponse(fattura);
    }

    @Override
    public Page<FatturaResponse> getByProdotto(Long idProdotto, int page, int size) {
        return null;
    }

    @Override
    public Page<FatturaResponse> search(FatturaSearchRequest request) {
        return null;
    }

    @Override
    @Transactional
    public FatturaResponse create(FatturaCreateRequest request) {
        Fattura entity = mapper.toEntity(request);
        entity = repository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    public FatturaResponse update(Long id, FatturaUpdateRequest request) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
