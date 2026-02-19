
package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.prodotto.ProdottoRequest;
import it.spindox.stagelab.magazzino.dto.prodotto.ProdottoResponse;
import it.spindox.stagelab.magazzino.entities.Prodotto;
import it.spindox.stagelab.magazzino.exceptions.ResourceNotFoundException;
import it.spindox.stagelab.magazzino.exceptions.prodottoexceptions.InvalidQuantityException;
import it.spindox.stagelab.magazzino.mappers.ProdottoMapper;
import it.spindox.stagelab.magazzino.repositories.ProdottoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;



@Slf4j
@Service
@RequiredArgsConstructor
public class ProdottoServiceImpl implements ProdottoService {

    private final ProdottoRepository repo;
    private final ProdottoMapper mapper;

    // GET /prodotti/list (paginazione)

    @Override
    @Transactional(readOnly = true)
    public Page<ProdottoResponse> getAllPaged(int page, int size) {

        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Parametri di paginazione non validi: page=" + page + ", size=" + size);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("nome"));
        Page<Prodotto> result = repo.findAll(pageable);

        return new PageImpl<>(
                result.getContent().stream()
                        .map(mapper::toResponse)
                        .toList(),
                pageable,
                result.getTotalElements()
        );
    }

    // GET /prodotti (solo ID)

    @Override
    @Transactional(readOnly = true)
    public Page<Long> searchIds(ProdottoRequest r) {
        Pageable pageable = PageRequest.of(r.getPage(), r.getSize());

        return repo.searchIds(
                r.getNome(),
                r.getDescrizione(),
                r.getPrezzoMin(),
                r.getPrezzoMax(),
                pageable
        );
    }

    // GET /prodotti

    @Override
    @Transactional(readOnly = true)
    public ProdottoResponse getById(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID prodotto non valido: " + id);
        }

        Prodotto p = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato: " + id));

        return mapper.toResponse(p);
    }

    // POST /prodotti

    @Override
    @Transactional
    public void create(ProdottoRequest req) {

        if (req.getQuantita() != null && req.getQuantita() < 0) {
            throw new InvalidQuantityException(null, req.getQuantita(), "Quantità non può essere negativa");
        }

        if (req.getScortaMinima() != null && req.getScortaMinima() < 0) {
            throw new InvalidQuantityException(null, req.getScortaMinima(), "Scorta minima non può essere negativa");
        }

        repo.save(mapper.toEntity(req));
    }

    // PATCH /prodotti

    @Override
    @Transactional
    public void update(Long id, ProdottoRequest req) {

        Prodotto p = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato: " + id));

        if (req.getQuantita() != null && req.getQuantita() < 0) {
            throw new InvalidQuantityException(id, req.getQuantita(), "Quantità non può essere negativa");
        }

        if (req.getScortaMinima() != null && req.getScortaMinima() < 0) {
            throw new InvalidQuantityException(id, req.getScortaMinima(), "Scorta minima non può essere negativa");
        }

        mapper.updateEntity(p, req);
        repo.save(p);
    }

    // DELETE /prodotti

    @Override
    @Transactional
    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID prodotto non valido: " + id);
        }
        repo.deleteById(id);
    }

    // POST /prodotti/search

    @Override
    @Transactional(readOnly = true)
    public Page<ProdottoResponse> search(ProdottoRequest r) {

        Pageable pageable = PageRequest.of(r.getPage(), r.getSize());

        return repo.search(
                r.getNome(),
                r.getDescrizione(),
                r.getPrezzoMin(),
                r.getPrezzoMax(),
                pageable
        ).map(mapper::toResponse);
    }
}
