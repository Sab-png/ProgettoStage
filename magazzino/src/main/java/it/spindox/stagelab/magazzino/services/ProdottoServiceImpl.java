
package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.prodotto.ProdottoRequest;
import it.spindox.stagelab.magazzino.dto.prodotto.ProdottoResponse;
import it.spindox.stagelab.magazzino.entities.Prodotto;
import it.spindox.stagelab.magazzino.exceptions.ResourceNotFoundException;
import it.spindox.stagelab.magazzino.mappers.ProdottoMapper;
import it.spindox.stagelab.magazzino.repositories.ProdottoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;



@Slf4j
@Service
@RequiredArgsConstructor
public class ProdottoServiceImpl implements ProdottoService {

    private final ProdottoRepository repo;
    private final ProdottoMapper mapper;

    // ===============================================================
    // GET ALL PAGED + STREAM (GET /prodotti/list)
    // ===============================================================
    @Override
    @Transactional(readOnly = true)
    public Page<ProdottoResponse> getAllPaged(int page, int size) {

        // Costruzione pageable con ordinamento per nome
        Pageable pageable = PageRequest.of(page, size, Sort.by("nome"));

        // Recupero paginato dal repository
        Page<Prodotto> result = repo.findAll(pageable);

        // STREAM: Entity -> DTO
        List<ProdottoResponse> list = result.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        // Ritorno il risultato paginato
        return new PageImpl<>(list, pageable, result.getTotalElements());
    }

    // ===============================================================
    // GET SOLO ID FILTRATI (GET /prodotti)
    // ===============================================================
    @Override
    public Page<Long> searchIds(ProdottoRequest r) {

        Pageable pageable = PageRequest.of(r.getPage(), r.getSize());

        // JPQL custom nel repository che restituisce solo gli ID
        return repo.searchIds(
                r.getNome(),
                r.getDescrizione(),
                r.getPrezzoMin(),
                r.getPrezzoMax(),
                pageable
        );
    }

    // ===============================================================
    // GET BY ID (GET /prodotti/{id})
    // ===============================================================
    @Override
    public ProdottoResponse getById(Long id) {

        Prodotto entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato"));

        return mapper.toResponse(entity);
    }

    // ===============================================================
    // CREATE (POST /prodotti)
    // ===============================================================
    @Override
    public void create(ProdottoRequest req) {
        repo.save(mapper.toEntity(req));
    }

    // ===============================================================
    // UPDATE (PATCH /prodotti/{id})
    // ===============================================================
    @Override
    public void update(Long id, ProdottoRequest req) {

        Prodotto p = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato"));

        mapper.updateEntity(p, req);

        repo.save(p);
    }

    // ===============================================================
    // DELETE (DELETE /prodotti/{id})
    // ===============================================================
    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // ===============================================================
    // SEARCH COMPLETA (POST /prodotti/search)
    // ===============================================================
    @Override
    public Page<ProdottoResponse> search(ProdottoRequest r) {

        Pageable pageable = PageRequest.of(r.getPage(), r.getSize());

        // JPQL search + mapping a DTO
        return repo.search(
                r.getNome(),
                r.getDescrizione(),
                r.getPrezzoMin(),
                r.getPrezzoMax(),
                pageable
        ).map(mapper::toResponse);
    }
}