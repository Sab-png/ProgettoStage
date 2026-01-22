
package it.spindox.stagelab.magazzino.services;

import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoResponse;
import it.spindox.stagelab.magazzino.entities.Magazzino;
import it.spindox.stagelab.magazzino.exceptions.ResourceNotFoundException;
import it.spindox.stagelab.magazzino.mappers.MagazzinoMapper;
import it.spindox.stagelab.magazzino.repositories.MagazzinoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MagazzinoServiceImpl implements MagazzinoService {

    private final MagazzinoRepository repository;
    private final MagazzinoMapper mapper;

    @Override
    public MagazzinoResponse getById(Long id) {
        Magazzino magazzino = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Magazzino non trovato"));
        return mapper.toResponse(magazzino);
    }

    @Override
    public void create(@Valid MagazzinoRequest request) {
        repository.save(mapper.toEntity(request));
    }

    @Override public void update(Long id, @Valid MagazzinoRequest request) {
        Magazzino magazzino = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Magazzino non trovato"));

        mapper.updateEntity(magazzino, request);
        repository.save(magazzino);
    }

    @Override
    public Page<MagazzinoResponse> search(@Valid MagazzinoRequest request) {
        int page = request.getPage() != 0 ? request.getPage() : 0;
        int size = request.getSize() != 0 ? request.getSize() : 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<Magazzino> pageEntity = repository.search(
                request.getId(),
                emptyToNull(request.getNome()),
                emptyToNull(request.getIndirizzo()),
                request.getCapacitaMin(),
                request.getCapacitaMax(),
                pageable
        );

        return pageEntity.map(mapper::toResponse);
    }@Override
    public void update(Long id, Magazzino request) {

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
