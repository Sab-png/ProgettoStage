package it.spindox.stagelab.magazzino.services;

import it.spindox.stagelab.magazzino.dto.request.MagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.request.MagazzinoSearchRequest;
import it.spindox.stagelab.magazzino.dto.response.MagazzinoResponse;
import it.spindox.stagelab.magazzino.entities.Magazzino;
import it.spindox.stagelab.magazzino.mappers.MagazzinoMapper;
import it.spindox.stagelab.magazzino.repositories.MagazzinoRepository;
import it.spindox.stagelab.magazzino.services.MagazzinoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class MagazzinoServiceImpl implements MagazzinoService {

    private final MagazzinoRepository repository;

    public MagazzinoServiceImpl(MagazzinoRepository repository) {
        this.repository = repository;
    }

    @Override
    public MagazzinoResponse getMagazzino(Long id) {
        return repository.findById(id)
                .map(MagazzinoMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Magazzino non trovato"));
    }

    @Override
    public MagazzinoResponse getMagazzinoByProdotto(Long prodottoId) {
        Magazzino magazzino = repository.findByProdottoId(prodottoId);
        if (magazzino == null) {
            throw new EntityNotFoundException("Magazzino non trovato per il prodotto");
        }
        return MagazzinoMapper.toResponse(magazzino);
    }

    @Override
    public MagazzinoResponse saveMagazzino(MagazzinoRequest request) {
        Magazzino magazzino = new Magazzino();
        magazzino.setNome(request.getNome());
        magazzino.setIndirizzo(request.getIndirizzo());
        magazzino.setCapacita(request.getCapacita());

        return MagazzinoMapper.toResponse(repository.save(magazzino));
    }

    @Override
    public Page<MagazzinoResponse> searchMagazzino(MagazzinoSearchRequest request) {
        return repository.search(
                        request.getNome(),
                        request.getIndirizzo(),
                        PageRequest.of(request.getPage(), request.getSize())
                )
                .map(MagazzinoMapper::toResponse);
    }
}
