package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.mappers.ProdottoMagazzinoMapper;
import it.spindox.stagelab.magazzino.repositories.MagazzinoRepository;
import it.spindox.stagelab.magazzino.repositories.ProdottoMagazzinoRepository;
import it.spindox.stagelab.magazzino.repositories.ProdottoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import it.spindox.stagelab.magazzino.dto.prodottomagazzino.ProdottoMagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.prodottomagazzino.ProdottoMagazzinoResponse;
import it.spindox.stagelab.magazzino.dto.prodottomagazzino.ProdottoMagazzinoSearchRequest;
import it.spindox.stagelab.magazzino.entities.Magazzino;
import it.spindox.stagelab.magazzino.entities.Prodotto;
import it.spindox.stagelab.magazzino.entities.ProdottoMagazzino;
import it.spindox.stagelab.magazzino.entities.StockStatusMagazzino;
import it.spindox.stagelab.magazzino.exceptions.ResourceNotFoundException;
import it.spindox.stagelab.magazzino.exceptions.prodottoexceptions.InvalidQuantityException;
import it.spindox.stagelab.magazzino.exceptions.prodottomagazzinoexceptions.CapacityExceededException;
import it.spindox.stagelab.magazzino.exceptions.prodottomagazzinoexceptions.DuplicateProdottoMagazzinoException;
import org.springframework.data.domain.*;




@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProdottoMagazzinoServiceImpl implements ProdottoMagazzinoService {

    private final ProdottoMagazzinoRepository repo;
    private final ProdottoRepository prodottoRepo;
    private final MagazzinoRepository magazzinoRepo;
    private final ProdottoMagazzinoMapper mapper;

    //  METODO PER IL RICALCOLO ATTUALE PER LO STOCK_STATUS DI  MAGAZZINO

    //Calcola la percentuale di riempimento del magazzino:
    // percentuale = (totale_pezzi / capacità) * 100


    private void updateStockStatus(Long magazzinoId) {

        Magazzino m = magazzinoRepo.findById(magazzinoId)
                .orElseThrow(() -> new ResourceNotFoundException("Magazzino non trovato: id=" + magazzinoId));

        Integer capacita = m.getCapacita();
        Integer totale = repo.sumQuantitaInMagazzino(magazzinoId); // mai null

        // Nessuna capacità = magazzino sempre ROSSO

        if (capacita == null || capacita <= 0) {
            m.setStockStatus(StockStatusMagazzino.ROSSO);
            magazzinoRepo.save(m);
            log.info("[MAG-STOCK] magazzino={} capacità nulla → ROSSO", magazzinoId);
            return;
        }

        double percent = (totale * 100.0) / capacita;

        StockStatusMagazzino status = StockStatusMagazzino.fromPercentuale(percent);

        m.setStockStatus(status);
        magazzinoRepo.save(m);

        log.info("[MAG-STOCK] magazzino={} capacità={} totale={} percent={} → {}",
                magazzinoId, capacita, totale, percent, status);
    }

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


    // SEARCH IDS


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
                .orElseThrow(() ->
                        new ResourceNotFoundException("Record Prodotto-Magazzino non trovato: id=" + id));

        return mapper.toResponse(entity);
    }

    // CREATE (INSERT)


    @Override
    @Transactional
    public void create(ProdottoMagazzinoRequest r) {

        log.info("[CREATE PM] prodottoId={}, magazzinoId={}, quantita={}",
                r.getProdottoId(), r.getMagazzinoId(), r.getQuantita());

        Prodotto prodotto = prodottoRepo.findById(r.getProdottoId())
                .orElseThrow(() -> ResourceNotFoundException.byId("Prodotto", r.getProdottoId()));

        Magazzino magazzino = magazzinoRepo.findById(r.getMagazzinoId())
                .orElseThrow(() -> ResourceNotFoundException.byId("Magazzino", r.getMagazzinoId()));

        // Evita I duplicati

        if (repo.existsByProdottoIdAndMagazzinoId(r.getProdottoId(), r.getMagazzinoId())) {
            throw new DuplicateProdottoMagazzinoException(r.getProdottoId(), r.getMagazzinoId());
        }

        // Validazione quantità

        Integer q = r.getQuantita();
        if (q == null || q < 0) {
            throw new InvalidQuantityException(r.getProdottoId(), q,
                    "Quantità non può essere negativa o nulla");
        }

        // Controllo della capacità

        Integer somma = repo.sumQuantitaInMagazzino(magazzino.getId());
        Integer capacita = magazzino.getCapacita() == null ? 0 : magazzino.getCapacita();

        if (capacita > 0 && (somma + q) > capacita) {
            throw new CapacityExceededException(magazzino.getId(), capacita, somma + q);
        }

        ProdottoMagazzino entity = mapper.toEntity(r);
        entity.setProdotto(prodotto);
        entity.setMagazzino(magazzino);

        repo.save(entity);

        //  AGGIORNO STOCK_STATUS DEL MAGAZZINO
        updateStockStatus(magazzino.getId());

        log.info("[CREATE PM OK] pmId={} prodotto={} magazzino={}",
                entity.getId(), prodotto.getId(), magazzino.getId());
    }

    // UPDATE (PATCH)


    @Override
    @Transactional
    public void update(Long id, ProdottoMagazzinoRequest r) {

        ProdottoMagazzino entity = repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Record Prodotto-Magazzino non trovato: id=" + id));

        // Validazione quantità

        if (r.getQuantita() != null && r.getQuantita() < 0) {
            throw new InvalidQuantityException(id, r.getQuantita(),
                    "Quantità non può essere negativa");
        }

        mapper.updateEntity(entity, r);

        // Ricontrollo capacità se aggiornata la quantità

        if (r.getQuantita() != null) {

            Long magazzinoId = entity.getMagazzino().getId();
            Integer somma = repo.sumQuantitaInMagazzino(magazzinoId);
            Integer capacita = entity.getMagazzino().getCapacita();

            if (capacita != null && capacita > 0 && somma > capacita) {
                throw new CapacityExceededException(magazzinoId, capacita, somma);
            }
        }

        repo.save(entity);

        //  AGGIORNO STOCK_STATUS DEL MAGAZZINO
        updateStockStatus(entity.getMagazzino().getId());

        log.info("[UPDATE PM OK] pmId={}", id);
    }

    // DELETE


    @Override
    @Transactional
    public void delete(Long id) {

        ProdottoMagazzino entity = repo.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Record Prodotto-Magazzino non trovato: id=" + id));

        Long magazzinoId = entity.getMagazzino().getId();

        repo.delete(entity);

        // Dopo la DELETE il totale cambia e aggiorna lo status
        updateStockStatus(magazzinoId);

        log.info("[DELETE PM OK] id={}", id);
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