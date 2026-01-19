
package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.prodotto.*;
import it.spindox.stagelab.magazzino.entities.Prodotto;
import it.spindox.stagelab.magazzino.exceptions.ResourceNotFoundException;
import it.spindox.stagelab.magazzino.mappers.ProdottoMapper;
import it.spindox.stagelab.magazzino.repositories.ProdottoRepository;
import it.spindox.stagelab.magazzino.services.ProdottoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

/**
 * Implementazione del service Prodotto.
 */
@Service
@RequiredArgsConstructor
public class ProdottoServiceImpl implements ProdottoService {

    private final ProdottoRepository repository;
    private final ProdottoMapper mapper;

    @Override
    public ProdottoResponse getById(Long id) {
        Prodotto prodotto = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato"));
        return mapper.toResponse(prodotto);
    }

    @Override
    public void create(ProdottoCreateRequest request) {
        repository.save(mapper.toEntity(request));
    }

    @Override
    public void update(Long id, ProdottoUpdateRequest request) {
        Prodotto prodotto = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato"));
        mapper.updateEntity(prodotto, request);
        repository.save(prodotto);
    }

    @Override
    public Page<ProdottoResponse> search(ProdottoSearchRequest request) {
        return null;
    }


}

