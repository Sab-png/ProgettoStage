package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.prodottomagazzino.ProdottoMagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.prodottomagazzino.ProdottoMagazzinoSearchRequest;
import it.spindox.stagelab.magazzino.entities.Magazzino;
import it.spindox.stagelab.magazzino.entities.StockStatusMagazzino;
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
import it.spindox.stagelab.magazzino.dto.prodottomagazzino.ProdottoMagazzinoResponse;
import it.spindox.stagelab.magazzino.entities.Prodotto;
import it.spindox.stagelab.magazzino.entities.ProdottoMagazzino;
import it.spindox.stagelab.magazzino.exceptions.jobsexceptions.InvalidCapacityException;
import it.spindox.stagelab.magazzino.entities.*;



@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProdottoMagazzinoServiceImpl implements ProdottoMagazzinoService {

    private final ProdottoMagazzinoRepository repo;
    private final ProdottoRepository prodottoRepo;
    private final MagazzinoRepository magazzinoRepo;
    private final ProdottoMagazzinoMapper mapper;



    //  VALIDATE + ALIGN

    private void validateAndAlign(ProdottoMagazzino e, boolean isCreate) {
        Integer q = e.getQuantita();

        // Negativi: errore

        if (q != null && q < 0) {
            throw new InvalidCapacityException(
                    e.getProdotto() != null ? e.getProdotto().getId() : null,
                    q,
                    "Quantità non può essere negativa"
            );
        }

        //  In create quantita non puo' essere NULL

        if (isCreate && q == null) {
            throw new InvalidCapacityException(
                    e.getProdotto() != null ? e.getProdotto().getId() : null,
                    null,
                    "Quantità non può essere null in creazione"
            );
        }
    }



    //  GET BY ID

    @Override
    @Transactional(readOnly = true)
    public ProdottoMagazzinoResponse getById(Long id) {
        ProdottoMagazzino entity = repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Record Prodotto-Magazzino non trovato: id=" + id));

        return mapper.toResponse(entity);
    }

// CRUD

    //  CREATE

    @Override
    public void create(ProdottoMagazzinoRequest r) {

        Prodotto prodotto = prodottoRepo.findById(r.getProdottoId())
                .orElseThrow(() -> ResourceNotFoundException.byId("Prodotto", r.getProdottoId()));

        Magazzino magazzino = magazzinoRepo.findById(r.getMagazzinoId())
                .orElseThrow(() -> ResourceNotFoundException.byId("Magazzino", r.getMagazzinoId()));

        if (r.getQuantita() != null && r.getQuantita() < 0)
            throw new InvalidCapacityException(r.getProdottoId(), r.getQuantita(), "Quantità non valida");

        ProdottoMagazzino entity = mapper.toEntity(r);
        entity.setProdotto(prodotto);
        entity.setMagazzino(magazzino);

        validateAndAlign(entity, true);

        // CALCOLA E SALVA SCORTA_MIN_STATUS

        ScortaMinPMStatus status = ScortaMinPMStatus.fromQuantita(entity.getQuantita());
        entity.setScortaMinStatus(status != null ? status.name() : null);

        repo.save(entity);
        updateStockStatus(magazzino.getId());
    }


    //  UPDATE

    @Override
    public void update(Long id, ProdottoMagazzinoRequest r) {

        ProdottoMagazzino entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record non trovato: id=" + id));

        if (r.getQuantita() != null && r.getQuantita() < 0)
            throw new InvalidCapacityException(
                    entity.getProdotto() != null ? entity.getProdotto().getId() : null,
                    r.getQuantita(),
                    "Quantità non valida"
            );

        mapper.updateEntity(entity, r);

        validateAndAlign(entity, false);

        //  AGGIORNA LO STATO

        ScortaMinPMStatus status = ScortaMinPMStatus.fromQuantita(entity.getQuantita());
        entity.setScortaMinStatus(status != null ? status.name() : null);

        repo.save(entity);
        updateStockStatus(entity.getMagazzino().getId());
    }



    //  DELETE

    @Override
    public void delete(Long id) {
        ProdottoMagazzino entity = repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Record non trovato: id=" + id));

        Long magazzinoId = entity.getMagazzino().getId();
        repo.delete(entity);

        updateStockStatus(magazzinoId);
    }



    //  GET ALL PAGED

    @Override
    @Transactional(readOnly = true)
    public Page<ProdottoMagazzinoResponse> getAllPaged(int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1));
        Page<ProdottoMagazzino> result = repo.findAll(pageable);
        return result.map(mapper::toResponse);
    }



    //  SEARCH IDS

    @Override
    @Transactional(readOnly = true)
    public Page<Long> searchIds(ProdottoMagazzinoSearchRequest r) {
        Pageable pageable = PageRequest.of(
                r.getPage() != null ? r.getPage() : 0,
                r.getSize() != null ? r.getSize() : 10,
                Sort.by(Sort.Direction.DESC, "id")
        );

        return repo.searchIds(r.getProdottoId(), r.getMagazzinoId(), pageable);
    }



    //  SEARCH ALL

    @Override
    @Transactional(readOnly = true)
    public Page<ProdottoMagazzinoResponse> search(ProdottoMagazzinoSearchRequest r) {
        Pageable pageable = PageRequest.of(
                r.getPage() != null ? r.getPage() : 0,
                r.getSize() != null ? r.getSize() : 10,
                Sort.by(Sort.Direction.DESC, "id")
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



    //  MAGAZZINO STOCK STATUS

    private void updateStockStatus(Long magazzinoId) {

        Magazzino m = magazzinoRepo.findById(magazzinoId)
                .orElseThrow(() -> new ResourceNotFoundException("Magazzino non trovato: id=" + magazzinoId));

        Integer capacita = m.getCapacita();
        Integer totale = repo.sumQuantitaInMagazzino(magazzinoId);

        if (capacita == null || capacita <= 0) {
            m.setStockStatus(StockStatusMagazzino.ROSSO);
            magazzinoRepo.save(m);
            return;
        }

        double percent = (totale * 100.0) / capacita;
        StockStatusMagazzino status = StockStatusMagazzino.fromPercentuale(percent);

        m.setStockStatus(status);
        magazzinoRepo.save(m);
    }
}