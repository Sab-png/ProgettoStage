package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.ProdottoMagazzino.ProdottoMagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.ProdottoMagazzino.ProdottoMagazzinoResponse;
import it.spindox.stagelab.magazzino.dto.ProdottoMagazzino.ProdottoMagazzinoSearchRequest;
import it.spindox.stagelab.magazzino.entities.Magazzino;
import it.spindox.stagelab.magazzino.entities.Prodotto;
import it.spindox.stagelab.magazzino.entities.ProdottoMagazzino;
import it.spindox.stagelab.magazzino.exceptions.ResourceNotFoundException;
import it.spindox.stagelab.magazzino.mappers.ProdottoMagazzinoMapper;
import it.spindox.stagelab.magazzino.repositories.MagazzinoRepository;
import it.spindox.stagelab.magazzino.repositories.ProdottoMagazzinoRepository;
import it.spindox.stagelab.magazzino.repositories.ProdottoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProdottoMagazzinoServiceImpl implements ProdottoMagazzinoService {

    private final ProdottoMagazzinoRepository repository;
    private final ProdottoRepository prodottoRepository;
    private final MagazzinoRepository magazzinoRepository;
    private final ProdottoMagazzinoMapper mapper;

    @Override
    public ProdottoMagazzinoResponse getById(Long id) {
        ProdottoMagazzino pm = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock non trovato"));
        return mapper.toResponse(pm);
    }

    @Override
    public void create(@Valid ProdottoMagazzinoRequest request) {

        Prodotto prodotto = prodottoRepository.findById(request.getProdottoId())
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato"));

        Magazzino magazzino = magazzinoRepository.findById(request.getMagazzinoId())
                .orElseThrow(() -> new ResourceNotFoundException("Magazzino non trovato"));

        ProdottoMagazzino pm = mapper.toEntity(request);

        pm.setProdotto(prodotto);
        pm.setMagazzino(magazzino);

        repository.save(pm);
    }

    @Override
    public void update(Long id, @Valid ProdottoMagazzinoRequest request) {
        ProdottoMagazzino pm = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stock non trovato"));

        mapper.updateEntity(pm, request);

        repository.save(pm);
    }

    @Override
    public <ProdottoMagazzinoSearchRequest> Page<ProdottoMagazzinoResponse> search(ProdottoMagazzinoSearchRequest prodottoMagazzinoSearchRequest) {
        return null;
    }


    @Override
    public Page<ProdottoMagazzinoResponse> search(@Valid ProdottoMagazzinoSearchRequest request) {

        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() != null ? request.getSize() : 20;

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<ProdottoMagazzino> pageEntity = repository.search(
                request.getId(),
                request.getProdottoId(),
                request.getMagazzinoId(),
                request.getQuantitaMin(),
                request.getQuantitaMax(),
                emptyToNull(request.getNomeProdotto()),
                emptyToNull(request.getNomeMagazzino()),
                pageable
        );

        return pageEntity.map(mapper::toResponse);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            return; // idempotente: come in ProdottoServiceImpl
        }
        repository.deleteById(id);
    }

    @Override
    public void checkStockLevels() {

        List<ProdottoMagazzino> all = repository.findAll();

        all.stream()
                .filter(pm -> pm.getQuantita() != null && pm.getScortaMin() != null)
                .filter(pm -> pm.getQuantita() < pm.getScortaMin())
                .forEach(pm -> {
                    System.out.println("⚠️ Stock basso per: "
                            + (pm.getProdotto() != null ? pm.getProdotto().getNome() : "Prodotto?")
                            + " nel magazzino "
                            + (pm.getMagazzino() != null ? pm.getMagazzino().getNome() : "Magazzino?")
                            + " — quantita=" + pm.getQuantita()
                            + ", scortaMin=" + pm.getScortaMin());
                });
    }

    // ---- helpers ----
    private String emptyToNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}

