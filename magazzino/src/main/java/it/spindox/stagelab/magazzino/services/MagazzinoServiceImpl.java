package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoResponse;
import it.spindox.stagelab.magazzino.entities.Magazzino;
import it.spindox.stagelab.magazzino.exceptions.ResourceNotFoundException;
import it.spindox.stagelab.magazzino.mappers.MagazzinoMapper;
import it.spindox.stagelab.magazzino.repositories.MagazzinoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;




@Slf4j
@Service
@RequiredArgsConstructor
public class MagazzinoServiceImpl implements MagazzinoService {

    private final MagazzinoRepository repository;
    private final MagazzinoMapper mapper;

    // ===============================================================
    // GET ALL PAGED + STREAM (GET /magazzino/list)
    // ===============================================================
    @Override
    @Transactional(readOnly = true)
    public Page<MagazzinoResponse> getAllPaged(int page, int size) {

        // Pageable con fallback (page >=0, size >=1)
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.max(size, 1),
                Sort.by("nome").ascending()
        );

        Page<Magazzino> p = repository.findAll(pageable);

        // STREAM: Entity -> DTO
        List<MagazzinoResponse> content = p.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return new PageImpl<>(content, pageable, p.getTotalElements());
    }

    // ===============================================================
    // GET SOLO ID FILTRATI (GET /magazzino)
    // ===============================================================
    @Override
    @Transactional(readOnly = true)
    public Page<Long> searchIds(MagazzinoRequest request) {

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        return repository.searchIds(
                request.getNome(),
                request.getIndirizzo(),
                request.getCapacitaMin(),
                request.getCapacitaMax(),
                pageable
        );
    }

    // ===============================================================
    // GET BY ID (GET /magazzino/{id})
    // ===============================================================
    @Override
    public MagazzinoResponse getById(Long id) {

        Magazzino m = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Magazzino non trovato"));

        return mapper.toResponse(m);
    }

    // ===============================================================
    // CREATE (POST /magazzino)
    // ===============================================================
    @Override
    public void create(MagazzinoRequest request) {
        repository.save(mapper.toEntity(request));
    }

    // ===============================================================
    // UPDATE (PATCH /magazzino/{id})
    // ===============================================================
    @Override
    public void update(Long id, MagazzinoRequest request) {

        Magazzino m = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Magazzino non trovato"));

        mapper.updateEntity(m, request);
        repository.save(m);
    }

    // ===============================================================
    // DELETE (DELETE /magazzino/{id})
    // ===============================================================
    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    // ===============================================================
    // CHECK STOCK LEVELS (placeholder)
    // ===============================================================
    @Override
    public void checkStockLevels() {
        // Funzione pronta per logiche di controllo stock
    }

    // ===============================================================
    // SEARCH COMPLETA (POST /magazzino/search)
    // ===============================================================
    @Override
    public Page<MagazzinoResponse> search(MagazzinoRequest r) {

        Pageable pageable = PageRequest.of(r.getPage(), r.getSize());

        return repository.search(
                r.getId(),
                r.getNome(),
                r.getIndirizzo(),
                r.getCapacitaMin(),
                r.getCapacitaMax(),
                pageable
        ).map(mapper::toResponse);
    }
}