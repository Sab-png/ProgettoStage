package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoResponse;
import it.spindox.stagelab.magazzino.entities.*;
import it.spindox.stagelab.magazzino.exceptions.ResourceNotFoundException;
import it.spindox.stagelab.magazzino.exceptions.jobsexceptions.SystemException;
import it.spindox.stagelab.magazzino.exceptions.magazzinoexceptions.InvalidCapacityException;
import it.spindox.stagelab.magazzino.exceptions.prodottoexceptions.InvalidQuantityException;
import it.spindox.stagelab.magazzino.mappers.MagazzinoMapper;
import it.spindox.stagelab.magazzino.repositories.MagazzinoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;




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

    // PAGINAZIONE LIST

    @Override
    @Transactional(readOnly = true)

    public Page<MagazzinoResponse> getAllPaged(int page, int size) {

        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.max(size, 1),
                Sort.by(Sort.Direction.DESC, "id")
        );

        Page<Magazzino> pageEntities = repository.findAll(pageable);

        return new PageImpl<>(
                pageEntities.getContent().stream()
                        .map(mapper::toResponse)
                        .toList(),
                pageable,
                pageEntities.getTotalElements()
        );
    }

    // SOLO ID

    @Override
    @Transactional(readOnly = true)

    public Page<Long> searchIds(MagazzinoRequest req) {

        Pageable pageable = PageRequest.of(
                req.getPage(),
                req.getSize(),
                Sort.by(Sort.Direction.DESC, "id")
        );

        return repository.searchIds(
                req.getNome(),
                req.getIndirizzo(),
                req.getCapacitaMin(),
                req.getCapacitaMax(),
                pageable
        );
    }

    // SEARCH COMPLETA

    @Override
    @Transactional(readOnly = true)

    public Page<MagazzinoResponse> search(MagazzinoRequest request) {

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by(Sort.Direction.DESC, "id")
        );

        return repository.search(
                request.getNome(),
                request.getIndirizzo(),
                request.getCapacitaMin(),
                request.getCapacitaMax(),
                pageable
        ).map(mapper::toResponse);
    }


    //   LOGICA BUSINESS DEL JOB


    @Override
    @Transactional

    public void checkStockLevels() {

        List<Magazzino> magazzini = repository.findAll();

        for (Magazzino m : magazzini) {

            Integer cap = getInteger(m);

            // ZERO : SUCCESS con WARNING

            if (cap == 0) {
                log.warn("[CAPACITY WARNING] Magazzino={} ha capacità = 0 (successo con warning)",
                        m.getNome());
            }

            // POSITIVO : IT IS OK

            int totaleProdotti = getTotaleProdotti(m);

            double percentuale = (totaleProdotti * 100.0) / Math.max(cap, 1);

            StockStatusMagazzino status = StockStatusMagazzino.fromPercentuale(percentuale);

            m.setStockStatus(status);
            repository.save(m);

            log.info("[CHECK] Magazzino={} Totale={} Percentuale={} Stato={}",
                    m.getNome(),
                    totaleProdotti,
                    String.format("%.2f", percentuale),
                    status
            );
        }
    }

    private static @NonNull Integer getInteger(Magazzino m) {
        Integer cap = m.getCapacita();

        // NULL : FAILED

        if (cap == null) {
            throw new SystemException(
                    "Capacità NULL per il magazzino '" + m.getNome() + "' (ID=" + m.getId() + ")"
            );
        }

        // NEGATIVO : FAILED

        if (cap < 0) {
            throw new SystemException(
                    "Capacità NEGATIVA (" + cap + ") per il magazzino '" + m.getNome() + "' (ID=" + m.getId() + ")"
            );
        }
        return cap;
    }

    // Validazione quantità prodotti

    private static int getTotaleProdotti(Magazzino m) {
        int tot = 0;
        for (ProdottoMagazzino p : m.getProdottiMagazzino()) {

            Integer qta = p.getQuantita();

            if (qta == null || qta < 0) {
                throw new InvalidQuantityException(
                        p.getId(),
                        qta,
                        "Quantità non valida nel magazzino " + m.getNome()
                );
            }

            tot += qta;
        }
        return tot;
    }
}