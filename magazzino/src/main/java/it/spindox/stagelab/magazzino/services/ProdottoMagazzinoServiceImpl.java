package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.prodottomagazzino.ProdottoMagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.prodottomagazzino.ProdottoMagazzinoResponse;
import it.spindox.stagelab.magazzino.dto.prodottomagazzino.ProdottoMagazzinoSearchRequest;
import it.spindox.stagelab.magazzino.entities.Magazzino;
import it.spindox.stagelab.magazzino.entities.Prodotto;
import it.spindox.stagelab.magazzino.entities.ProdottoMagazzino;
import it.spindox.stagelab.magazzino.exceptions.ResourceNotFoundException;
import it.spindox.stagelab.magazzino.mappers.ProdottoMagazzinoMapper;
import it.spindox.stagelab.magazzino.repositories.MagazzinoRepository;
import it.spindox.stagelab.magazzino.repositories.ProdottoMagazzinoRepository;
import it.spindox.stagelab.magazzino.repositories.ProdottoRepository;
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
    private final ProdottoRepository prodottoRepo;
    private final MagazzinoRepository magazzinoRepo;
    private final ProdottoMagazzinoMapper mapper;

    // GET ALL PAGED


    @Override
    @Transactional(readOnly = true)

    public Page<ProdottoMagazzinoResponse> getAllPaged(int page, int size) {

        int safePage = Math.max(page, 0);
        int safeSize = Math.max(size, 1);

        Pageable pageable = PageRequest.of(safePage, safeSize);

        Page<ProdottoMagazzino> result = repo.findAll(pageable);

        List<ProdottoMagazzinoResponse> content = result.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return new PageImpl<>(content, pageable, result.getTotalElements());
    }

    // GET IDs FILTRATI


    @Override
    @Transactional(readOnly = true)

    public Page<Long> searchIds(ProdottoMagazzinoSearchRequest r) {

        int safePage = (r.getPage() == null || r.getPage() < 0) ? 0 : r.getPage();
        int safeSize = (r.getSize() == null || r.getSize() < 1) ? 10 : r.getSize();

        Pageable pageable = PageRequest.of(safePage, safeSize);

        return repo.searchIds(
                r.getProdottoId(),
                r.getMagazzinoId(),
                pageable
        );
    }


    // GET BY ID


    @Override
    @Transactional(readOnly = true)

    public ProdottoMagazzinoResponse getById(Long id) {

        ProdottoMagazzino entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record non trovato"));

        return mapper.toResponse(entity);
    }


    // CREATE


    @Override
    @Transactional

    public void create(ProdottoMagazzinoRequest r) {

        Prodotto prodotto = prodottoRepo.findById(r.getProdottoId())
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato"));

        Magazzino magazzino = magazzinoRepo.findById(r.getMagazzinoId())
                .orElseThrow(() -> new ResourceNotFoundException("Magazzino non trovato"));

        ProdottoMagazzino entity = mapper.toEntity(r);
        entity.setProdotto(prodotto);
        entity.setMagazzino(magazzino);

        repo.save(entity);
    }

    // UPDATE


    @Override
    @Transactional

    public void update(Long id, ProdottoMagazzinoRequest r) {

        ProdottoMagazzino entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record non trovato"));

        mapper.updateEntity(entity, r);

        repo.save(entity);
    }


    // DELETE


    @Override
    @Transactional

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Record non trovato");
        }
        repo.deleteById(id);
    }

    // SEARCH COMPLETA

    @Override
    @Transactional(readOnly = true)

    public Page<ProdottoMagazzinoResponse> search(ProdottoMagazzinoSearchRequest r) {

        int safePage = (r.getPage() == null || r.getPage() < 0) ? 0 : r.getPage();
        int safeSize = (r.getSize() == null || r.getSize() < 1) ? 10 : r.getSize();

        Pageable pageable = PageRequest.of(safePage, safeSize);

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