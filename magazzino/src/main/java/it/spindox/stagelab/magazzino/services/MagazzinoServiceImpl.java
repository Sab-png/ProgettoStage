
package it.spindox.stagelab.magazzino.services;

import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoResponse;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoSearchRequest;
import it.spindox.stagelab.magazzino.dto.magazzino.*;


import it.spindox.stagelab.magazzino.entities.Magazzino;
import it.spindox.stagelab.magazzino.exceptions.ResourceNotFoundException;
import it.spindox.stagelab.magazzino.mappers.MagazzinoMapper;
import it.spindox.stagelab.magazzino.repositories.MagazzinoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public void create(MagazzinoRequest request) {
        repository.save(mapper.toEntity(request));
    }

    @Override
    public void update(Long id, MagazzinoUpdateRequest request) {
        Magazzino magazzino = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Magazzino non trovato"));

        mapper.updateEntity(magazzino, request);
        repository.save(magazzino);
    }

    @Override
    public Page<MagazzinoResponse> search(MagazzinoSearchRequest request) {
        return Page.empty();
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Magazzino non trovato");
        }
        repository.deleteById(id);
    }
}
