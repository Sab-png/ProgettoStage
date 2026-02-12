package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.ProdottoMagazzino.ProdottoMagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.ProdottoMagazzino.ProdottoMagazzinoResponse;
import it.spindox.stagelab.magazzino.dto.ProdottoMagazzino.ProdottoMagazzinoSearchRequest;
import it.spindox.stagelab.magazzino.entities.ProdottoMagazzino;
import it.spindox.stagelab.magazzino.exceptions.ResourceNotFoundException;
import it.spindox.stagelab.magazzino.mappers.ProdottoMagazzinoMapper;
import it.spindox.stagelab.magazzino.repositories.ProdottoMagazzinoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;



@Service
@RequiredArgsConstructor
@Slf4j
public class ProdottoMagazzinoServiceImpl implements ProdottoMagazzinoService {

    private final ProdottoMagazzinoRepository repo;
    private final ProdottoMagazzinoMapper mapper;

    // ===============================================================
    // GET ALL PAGED + STREAM  (GET /prodotto-magazzino/list)
    // ===============================================================
    @Override
    @Transactional(readOnly = true)
    public Page<ProdottoMagazzinoResponse> getAllPaged(int page, int size) {

        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.max(size, 1)
        );

        Page<ProdottoMagazzino> result = repo.findAll(pageable);

        // STREAM mapping Entity -> DTO
        List<ProdottoMagazzinoResponse> content = result.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return new PageImpl<>(content, pageable, result.getTotalElements());
    }

    // ===============================================================
    // GET IDs FILTRATI (GET /prodotto-magazzino)
    // ===============================================================
    @Override
    @Transactional(readOnly = true)
    public Page<Long> searchIds(ProdottoMagazzinoSearchRequest r) {

        Pageable pageable = PageRequest.of(
                r.getPage(),
                r.getSize()
        );

        return repo.searchIds(
                r.getProdottoId(),
                r.getMagazzinoId(),
                pageable
        );
    }

    // ===============================================================
    // GET BY ID (GET /prodotto-magazzino/{id})
    // ===============================================================
    @Override
    @Transactional(readOnly = true)
    public ProdottoMagazzinoResponse getById(Long id) {

        ProdottoMagazzino entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record non trovato"));

        return mapper.toResponse(entity);
    }

    // ===============================================================
    // CREATE (POST /prodotto-magazzino)
    // ===============================================================
    @Override
    @Transactional
    public void create(ProdottoMagazzinoRequest r) {
        repo.save(mapper.toEntity(r));
    }

    // ===============================================================
    // UPDATE (PUT /prodotto-magazzino/{id})
    // ===============================================================
    @Override
    @Transactional
    public void update(Long id, ProdottoMagazzinoRequest r) {

        ProdottoMagazzino entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record non trovato"));

        mapper.updateEntity(entity, r);
        repo.save(entity);
    }

    // ===============================================================
    // DELETE (DELETE /prodotto-magazzino/{id})
    // ===============================================================
    @Override
    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Record non trovato");
        }
        repo.deleteById(id);
    }

    // ===============================================================
    // SEARCH COMPLETA → DTO COMPLETI (POST /prodotto-magazzino/search)
    // ===============================================================
    @Override
    @Transactional(readOnly = true)
    public Page<ProdottoMagazzinoResponse> search(ProdottoMagazzinoSearchRequest r) {

        Pageable pageable = PageRequest.of(
                r.getPage(),
                r.getSize()
        );

        Page<ProdottoMagazzino> result = repo.search(
                r.getId(),
                r.getProdottoId(),
                r.getMagazzinoId(),
                r.getQuantitaMin(),
                r.getQuantitaMax(),
                r.getNomeProdotto(),
                r.getNomeMagazzino(),
                pageable
        );

        return result.map(mapper::toResponse);
    }
}