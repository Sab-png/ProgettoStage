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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementazione del servizio per la gestione dei Magazzini.
 * Include la logica di controllo livelli di stock con aggiornamento stato (VERDE/GIALLO/ROSSO).
 */
@Service
@RequiredArgsConstructor
public class MagazzinoServiceImpl implements MagazzinoService {

    private final MagazzinoRepository repository;
    private final MagazzinoMapper mapper;
    private final ProdottoMagazzinoRepository prodottoMagazzinoRepository;

    @Value("${inventory.min.threshold:5}")
    private int defaultThreshold;

    // ANSI colori per log console
    private static final String RESET  = "\u001B[0m";
    private static final String GREEN  = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED    = "\u001B[31m";

    // CRUD Magazzino


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


    //              LOGICA DELLO STOCK CON COLORI ANSI

    @Override
    @Transactional
    public void checkStockLevels() {

        List<ProdottoMagazzino> lista = prodottoMagazzinoRepository.findAll();

        for (ProdottoMagazzino pm : lista) {

            int quantita = pm.getQuantita();
            int soglia = pm.getSogliaMinima() > 0 ? pm.getSogliaMinima() : defaultThreshold;

            StockStatus nuovoStatus;

            if (quantita == 0) {
                nuovoStatus = StockStatus.ROSSO;
            }
            else if (quantita < soglia) {
                nuovoStatus = StockStatus.GIALLO;
            }
            else {
                nuovoStatus = StockStatus.VERDE;
            }

            StockStatus attuale = pm.getStatus();

            // Aggiorna solo se lo stato è cambiato
            if (attuale != nuovoStatus) {
                pm.setStatus(nuovoStatus);
                prodottoMagazzinoRepository.save(pm);

                System.out.println(
                        GREEN + "Aggiornato stock: " + RESET +
                                "'" + pm.getProdotto().getNome() + "'" +
                                " | da " + attuale + " → " + nuovoStatus +
                                " | Q=" + quantita + " | soglia=" + soglia
                );
            }

            // Log colorati di situazione
            switch (nuovoStatus) {
                case GIALLO ->
                        System.out.println(YELLOW +
                                "SOTTO SOGLIA: '" + pm.getProdotto().getNome() +
                                "' (Q=" + quantita + ", soglia=" + soglia + ")" +
                                RESET);

                case ROSSO ->
                        System.out.println(RED +
                                "ESAURITO: '" + pm.getProdotto().getNome() + "'" +
                                RESET);

                case VERDE ->
                        System.out.println(GREEN +
                                "OK: '" + pm.getProdotto().getNome() + "' disponibile" +
                                RESET);
            }
        }
    }
}