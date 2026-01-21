
package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.prodotto.ProdottoRequest;
import it.spindox.stagelab.magazzino.dto.prodotto.ProdottoResponse;
import it.spindox.stagelab.magazzino.entities.Prodotto;
import it.spindox.stagelab.magazzino.exceptions.ResourceNotFoundException;
import it.spindox.stagelab.magazzino.mappers.ProdottoMapper;
import it.spindox.stagelab.magazzino.repositories.ProdottoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public void create(ProdottoRequest request) {
        repository.save(mapper.toEntity(request));
    }

    @Override
    public void update(Long id, @Valid ProdottoRequest request) {
        Prodotto prodotto = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato"));
        mapper.updateEntity(prodotto, request);
        repository.save(prodotto);
    }

    @Override
    public Page<ProdottoResponse> search(@Valid ProdottoRequest request) {
        // Paging/Sordinamento di default (puoi spostarlo nel Controller e passare un Pageable dinamico)
        Pageable pageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "id"));

        Page<Prodotto> page = repository.search(
                emptyToNull(request.getNome()),
                emptyToNull(request.getCodice()),
                emptyToNull(request.getCategoria()),
                pageable
        );

        return page.map(mapper::toResponse);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            return; // idempotente
        }
        repository.deleteById(id);
    }

    // ---- helpers ----
    private String emptyToNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}
