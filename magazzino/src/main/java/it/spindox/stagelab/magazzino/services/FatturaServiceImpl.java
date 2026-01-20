
package it.spindox.stagelab.magazzino.services;

import it.spindox.stagelab.magazzino.dto.fattura.FatturaCreateRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaSearchRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaUpdateRequest;
import it.spindox.stagelab.magazzino.entities.Fattura;
import it.spindox.stagelab.magazzino.exceptions.ResourceNotFoundException;
import it.spindox.stagelab.magazzino.mappers.FatturaMapper;
import it.spindox.stagelab.magazzino.repositories.FatturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FatturaServiceImpl implements FatturaService {

    private final FatturaRepository repository;
    private final FatturaMapper mapper;

    // =========================
    // READ
    // =========================

    @Override
    @Transactional(readOnly = true)
    public FatturaResponse getById(Long id) {
        Fattura fattura = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fattura non trovata"));
        return mapper.toResponse(fattura);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FatturaResponse> getByProdotto(Long idProdotto, int page, int size) {
        // Normalizzazione basilare (evita valori negativi/0 non validi)
        int safePage = Math.max(0, page);
        int safeSize = Math.max(1, size);

        Pageable pageable = PageRequest.of(safePage, safeSize);
        // Adegua al tuo modello: relazione diretta (prodottoId) o tramite righe
        Page<Fattura> fatture = repository.findByProdottoId(idProdotto, pageable);
        return fatture.map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FatturaResponse> search(FatturaSearchRequest request) {
        // Se FatturaSearchRequest usa Integer (wrapper), gestisci default qui:
        int page = request.getPage() != 0 ? request.getPage() : 0;
        int size = request.getSize() != 0 ? request.getSize() : 20;

        // Clamp per sicurezza
        page = Math.max(0, page);
        size = Math.max(1, size);

        Pageable pageable = PageRequest.of(page, size);

        // TODO: sostituisci con la tua logica reale (derivata/specification/querydsl)
        Page<Fattura> result = Page.empty(pageable);

        return result.map(mapper::toResponse);
    }

    // =========================
    // WRITE
    // =========================

    @Override
    @Transactional
    public FatturaResponse create(FatturaCreateRequest request) {
        Fattura entity = mapper.toEntity(request);

        // Se in CreateRequest c’è idProdotto e la tua entity ha relazione ManyToOne,
        // risolvi qui il Prodotto e setta entity.setProdotto(prodotto) prima del save.

        entity = repository.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional
    public FatturaResponse update(Long id, FatturaUpdateRequest request) {
        Fattura fattura = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fattura non trovata"));

        // Mutazione in-place: il mapper è correttamente void per update
        mapper.updateEntity(fattura, request);

        // Se l'update prevede cambio prodotto con idProdotto nel DTO,
        // risolvi qui il Prodotto e setta fattura.setProdotto(prodotto).

        fattura = repository.save(fattura);
        return mapper.toResponse(fattura);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Fattura non trovata");
        }
        repository.deleteById(id);
    }
}
