package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.magazzino.*;
import it.spindox.stagelab.magazzino.entities.Magazzino;
import it.spindox.stagelab.magazzino.exceptions.ResourceNotFoundException;
import it.spindox.stagelab.magazzino.mappers.MagazzinoMapper;
import it.spindox.stagelab.magazzino.repositories.MagazzinoRepository;
import it.spindox.stagelab.magazzino.services.MagazzinoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Implementazione del service Magazzino.
 */
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
    public void create(MagazzinoCreateRequest request) {
        repository.save(mapper.toEntity(request));
    }

    @Override
    public Page<MagazzinoResponse> search(MagazzinoSearchRequest request) {
        return null; // da implementare
    }
}
