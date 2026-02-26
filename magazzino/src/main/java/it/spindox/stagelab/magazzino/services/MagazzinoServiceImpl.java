package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoResponse;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoSearchRequest;
import it.spindox.stagelab.magazzino.entities.*;
import it.spindox.stagelab.magazzino.exceptions.ResourceNotFoundException;
import it.spindox.stagelab.magazzino.exceptions.jobsexceptions.InvalidCapacityException;
import it.spindox.stagelab.magazzino.mappers.MagazzinoMapper;
import it.spindox.stagelab.magazzino.repositories.MagazzinoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import it.spindox.stagelab.magazzino.entities.Magazzino;
import it.spindox.stagelab.magazzino.entities.ProdottoMagazzino;
import it.spindox.stagelab.magazzino.entities.StockStatusMagazzino;





@Slf4j
@Service
@RequiredArgsConstructor
public class MagazzinoServiceImpl implements MagazzinoService {

    private final MagazzinoRepository repository;
    private final MagazzinoMapper mapper;


    // GET BY ID

    @Override
    @Transactional(readOnly = true)
    public MagazzinoResponse getById(Long id) {
        Magazzino entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Magazzino non trovato"));
        return mapper.toResponse(entity);
    }


    // CREATE


    @Override
    @Transactional
    public void create(MagazzinoRequest request) {
        Magazzino entity = mapper.fromRequest(request);
        repository.save(entity);
    }


    // UPDATE


    @Override
    @Transactional
    public void update(Long id, MagazzinoRequest request) {
        Magazzino entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Magazzino non trovato"));
        mapper.updateEntity(entity, request);
        repository.save(entity);
    }


    // DELETE


    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Magazzino non trovato");
        }
        repository.deleteById(id);
    }


    // GET ALL PAGED


    @Override
    @Transactional(readOnly = true)
    public Page<MagazzinoResponse> getAllPaged(int page, int size) {

        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.max(size, 1),
                Sort.by(Sort.Direction.ASC, "id")
        );

        Page<Magazzino> pageEntities = repository.findAll(pageable);

        return pageEntities.map(mapper::toResponse);
    }


    // SEARCH IDS


    @Override
    @Transactional(readOnly = true)
    public Page<Long> searchIds(MagazzinoSearchRequest req) {

        Pageable pageable = PageRequest.of(
                req.getPage(),
                req.getSize(),
                Sort.by(Sort.Direction.ASC, "id")
        );

        return repository.searchIds(
                req.getNome(),
                req.getIndirizzo(),
                req.getCapacitaMin(),
                req.getCapacitaMax(),
                pageable
        );
    }


    // SEARCH COMPLETA (DTO)


    @Override
    @Transactional(readOnly = true)
    public Page<MagazzinoResponse> search(MagazzinoSearchRequest request) {

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by(Sort.Direction.ASC, "id")
        );

        return repository.search(
                request.getNome(),
                request.getIndirizzo(),
                request.getCapacitaMin(),
                request.getCapacitaMax(),
                pageable
        ).map(mapper::toResponse);
    }


    // LOGICA BUSINESS DEL JOB


    @Override
    @Transactional
    public void checkStockLevels() {

        List<Magazzino> magazzini = repository.findAll();
        List<String> errori = new ArrayList<>();

        for (Magazzino m : magazzini) {
            try {
                validaEModificaMagazzino(m);
            } catch (InvalidCapacityException e) {
                errori.add("Magazzino " + m.getNome() + " (ID=" + m.getId() + "): " + e.getMessage());
            }
        }

        if (!errori.isEmpty()) {
            throw new InvalidCapacityException(
                    (Long) null,
                    null,
                    "Rilevate anomalie in più magazzini:\n - " + String.join("\n - ", errori)
            );
        }
    }

    private void validaEModificaMagazzino(Magazzino m) {

        Integer cap = getamountcapacity(m);

        if (cap == 0) {
            log.warn("[CAPACITY WARNING] Magazzino={} ha capacità = 0", m.getNome());
        }

        int totale = getTotaleProdotti(m);
        double percentuale = (totale * 100.0) / Math.max(cap, 1);

        StockStatusMagazzino status = StockStatusMagazzino.fromPercentuale(percentuale);

        m.setStockStatus(status);
        repository.save(m);
    }

    private Integer getamountcapacity(Magazzino m) {
        Integer cap = m.getCapacita();

        if (cap == null) {
            throw new InvalidCapacityException(
                    (Long) null, null,
                    "Capacità NULL per il magazzino '" + m.getNome() + "' (ID=" + m.getId() + ")"
            );
        }

        if (cap < 0) {
            throw new InvalidCapacityException(
                    (Long) null, cap,
                    "Capacità NEGATIVA (" + cap + ") per il magazzino '" + m.getNome() + "' (ID=" + m.getId() + ")"
            );
        }

        return cap;
    }

    private int getTotaleProdotti(Magazzino m) {
        int tot = 0;

        for (ProdottoMagazzino p : m.getProdottiMagazzino()) {
            Integer qta = p.getQuantita();

            if (qta == null || qta < 0) {
                throw new InvalidCapacityException(
                        p.getId(),
                        qta,
                        "Quantità non valida nel magazzino " + m.getNome()
                                + " (ID=" + m.getId() + ")"
                );
            }

            tot += qta;
        }
        return tot;
    }
}