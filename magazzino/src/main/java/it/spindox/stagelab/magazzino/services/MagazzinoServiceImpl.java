package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoResponse;
import it.spindox.stagelab.magazzino.entities.Magazzino;
import it.spindox.stagelab.magazzino.entities.ProdottoMagazzino;
import it.spindox.stagelab.magazzino.entities.StockStatus;
import it.spindox.stagelab.magazzino.exceptions.ResourceNotFoundException;
import it.spindox.stagelab.magazzino.mappers.MagazzinoMapper;
import it.spindox.stagelab.magazzino.repositories.MagazzinoRepository;
import it.spindox.stagelab.magazzino.repositories.ProdottoMagazzinoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;



/**
 * Implementazione del servizio per la gestione dei Magazzini.
 * Include la logica di controllo livelli di stock con aggiornamento stato (VERDE/GIALLO/ROSSO).
 *
 * Regole:
 * - quantita == null     → ignora (no DB update, no log critici)
 * - quantita == 0        → ROSSO (log ERROR)
 * - quantita < soglia    → GIALLO (log WARN se soglia > 5)
 * - quantita >= soglia   → VERDE (apposto)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MagazzinoServiceImpl implements MagazzinoService {

    private final MagazzinoRepository repository;
    private final MagazzinoMapper mapper;
    private final ProdottoMagazzinoRepository prodottoMagazzinoRepository;

    @Value("${inventory.min.threshold:5}")
    private int defaultThreshold;

    // CRUD MAGAZZINO


    @Override
    @Transactional(readOnly = true)
    public MagazzinoResponse getById(Long id) {
        Magazzino magazzino = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Magazzino non trovato"));
        return mapper.toResponse(magazzino);
    }

    @Override
    @Transactional
    public void create(@Valid MagazzinoRequest request) {
        repository.save(mapper.toEntity(request));
    }

    @Override
    @Transactional
    public void update(Long id, @Valid MagazzinoRequest request) {
        Magazzino magazzino = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Magazzino non trovato"));
        mapper.updateEntity(magazzino, request);
        repository.save(magazzino);
    }

    @Override
    @Transactional(readOnly = true)
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
    }

    private String emptyToNull(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) return;
        repository.deleteById(id);
    }

    // LOGICA CONTROLLO STOCK


    /**
     * Scorre tutti i ProdottoMagazzino
     */
    @Override
    @Transactional
    public void checkStockLevels() {
        List<ProdottoMagazzino> lista = prodottoMagazzinoRepository.findAll();
        lista.forEach(this::processSingleStockItem);
    }

    /** Processa un singolo record di ProdottoMagazzino. */
    private void processSingleStockItem(ProdottoMagazzino pm) {
        // 1) Se non c’è quantità, non segniamo nulla (no DB, no log critici)

        if (pm.getQuantita() == null) {
            log.debug("Quantità assente: skip | idProdottoMagazzino={}", pm.getId());
            return;
        }

        // 2) Calcola soglia effettiva

        int soglia = computeEffectiveThreshold(pm);

        // 3) Determina lo stato

        StockStatus nuovo = StockStatus.fromQuantita(pm.getQuantita(), soglia);
        if (nuovo == null) {
            log.debug("Quantità non valutabile: skip | idProdottoMagazzino={}", pm.getId());
            return;
        }

        StockStatus attuale = pm.getStatus();

        // 4) Aggiorna DB solo se necessario (GIALLO/ROSSO) e lo stato è cambiato

        updateStatusIfNeeded(pm, attuale, nuovo, soglia);

        // 5) Log per il livello di gravità (GIALLO → WARN solo se soglia > 5)
        logStatus(pm, nuovo, soglia);
    }

    /** Soglia effettiva*/

    private int computeEffectiveThreshold(ProdottoMagazzino pm) {
        Integer s = pm.getSogliaMinima();
        return (s != null && s > 0) ? s : defaultThreshold;
    }

    /**
     * Aggiorna il DB solo se:
     * - lo stato è cambiato
     * - (GIALLO/ROSSO)
     * Mai  VERDE.
     */

    private void updateStatusIfNeeded(ProdottoMagazzino pm, StockStatus attuale, StockStatus nuovo, int soglia) {
        if (!Objects.equals(attuale, nuovo) && shouldPersist(nuovo)) {
            pm.setStatus(nuovo);
            prodottoMagazzinoRepository.save(pm);

            var names = getSafeNames(pm);
            log.info(
                    "Aggiornato stock | prodotto='{}' | magazzino='{}' | {} → {} | Q={} | soglia={}",
                    names.nomeProdotto(),
                    names.nomeMagazzino(),
                    attuale,
                    nuovo,
                    pm.getQuantita(),
                    soglia
            );
        }
    }

    /** Si controllano solo stati non-OK (no VERDI). */

    private boolean shouldPersist(StockStatus status) {
        return status == StockStatus.GIALLO || status == StockStatus.ROSSO;
    }

    /** Logga la situazione corrente (WARN per GIALLO solo se soglia < 5). */

    private void logStatus(ProdottoMagazzino pm, StockStatus status, int soglia) {
        var names = getSafeNames(pm);
        int q = pm.getQuantita();

        switch (status) {
            case ROSSO -> log.error(
                    "ESAURITO | prodotto='{}' | magazzino='{}' | Q=0",
                    names.nomeProdotto(), names.nomeMagazzino()
            );

            case GIALLO -> {
                if (soglia < 5) {
                    log.warn(
                            "SOTTO SOGLIA | prodotto='{}' | magazzino='{}' | Q={} | soglia={}",
                            names.nomeProdotto(), names.nomeMagazzino(), q, soglia
                    );
                } else {
                    log.debug(
                            "SOTTO SOGLIA (silenced, soglia<=5) | prodotto='{}' | magazzino='{}' | Q={} | soglia={}",
                            names.nomeProdotto(), names.nomeMagazzino(), q, soglia
                    );
                }
            }

            case VERDE -> log.debug(
                    "OK | prodotto='{}' | magazzino='{}' | Q={}",
                    names.nomeProdotto(), names.nomeMagazzino(), q
            );
        }
    }

    /** Estrattore nomi sicuri per i log, */

    private Names getSafeNames(ProdottoMagazzino pm) {
        String prodotto = (pm.getProdotto() != null && pm.getProdotto().getNome() != null)
                ? pm.getProdotto().getNome()
                : "Prodotto sconosciuto";

        String magazzino = (pm.getMagazzino() != null && pm.getMagazzino().getNome() != null)
                ? pm.getMagazzino().getNome()
                : "Magazzino sconosciuto";

        return new Names(prodotto, magazzino);
    }

    /** Semplice record per restituire i nomi. */
    private record Names(String nomeProdotto, String nomeMagazzino) {}
}