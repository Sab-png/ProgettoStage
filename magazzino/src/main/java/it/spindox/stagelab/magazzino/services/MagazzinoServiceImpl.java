
package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoResponse;
import it.spindox.stagelab.magazzino.entities.Magazzino;
import it.spindox.stagelab.magazzino.exceptions.ResourceNotFoundException;
import it.spindox.stagelab.magazzino.mappers.MagazzinoMapper;
import it.spindox.stagelab.magazzino.repositories.MagazzinoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;

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
    public void update(Long id, @Valid Magazzino request) {
        Magazzino magazzino = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Magazzino non trovato"));

        mapper.updateEntity(magazzino, request);
        repository.save(magazzino);
    }


    @Override
    public Page<MagazzinoResponse> search(@Valid MagazzinoRequest request) {
        Page<Magazzino> page = repository.search(
                request.getNome(),
                request.getCodice(),
                (Pageable) PageRequest.of(0, 20)
        );

        return page.map(mapper::toResponse);
    }



    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
          return;
        }
        repository.deleteById(id);
    }
}
