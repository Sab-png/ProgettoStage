
package it.spindox.stagelab.magazzino.services;

import it.spindox.stagelab.magazzino.dto.fattura.FatturaRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;
import it.spindox.stagelab.magazzino.entities.Fattura;
import it.spindox.stagelab.magazzino.entities.Prodotto;
import it.spindox.stagelab.magazzino.exceptions.ResourceNotFoundException;
import it.spindox.stagelab.magazzino.mappers.FatturaMapper;
import it.spindox.stagelab.magazzino.repositories.FatturaRepository;
import it.spindox.stagelab.magazzino.repositories.ProdottoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FatturaServiceImpl implements FatturaService {

    private final FatturaRepository fatturaRepository;
    private final ProdottoRepository prodottoRepository;
    private final FatturaMapper fatturaMapper;

    // SEARCH
    @Override
    public Page<FatturaResponse> search(FatturaRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by(Sort.Direction.DESC, "dataFattura")
        );

        // Converte eventuali Double -> BigDecimal se il DTO espone Double
        BigDecimal importoMin = request.getImportoMin() != null
                ? new BigDecimal(request.getImportoMin().toString())
                : null;

        BigDecimal importoMax = request.getImportoMax() != null
                ? new BigDecimal(request.getImportoMax().toString())
                : null;

        Page<Fattura> page = fatturaRepository.search(
                request.getNumero(),
                request.getIdProdotto(),
                request.getDataFrom(),
                request.getDataTo(),
                importoMin,
                importoMax,
                pageable
        );

        List<FatturaResponse> content = page.getContent()
                .stream()
                .map(fatturaMapper::toResponse)
                .toList();

        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }

    // CREATE
    @Override
    public FatturaResponse create(FatturaRequest request) {
        if (request.getNumero() == null || request.getNumero().isBlank()) {
            throw new IllegalArgumentException("Numero fattura obbligatorio");
        }
        if (request.getIdProdotto() == null) {
            throw new IllegalArgumentException("Id prodotto obbligatorio");
        }

        Prodotto prodotto = prodottoRepository.findById(request.getIdProdotto())
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato"));

        Fattura entity = fatturaMapper.toEntity(request, prodotto);
        entity = fatturaRepository.save(entity);
        return fatturaMapper.toResponse(entity);
    }

    // UPDATE (PATCH)
    @Override
    public FatturaResponse update(Long id, FatturaRequest request) throws Throwable {
        Fattura entity = fatturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fattura non trovata"));

        Prodotto prodotto = null;
        if (request.getIdProdotto() != null) {
            prodotto = prodottoRepository.findById(request.getIdProdotto())
                    .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato"));
        }

        fatturaMapper.updateEntity(entity, request, prodotto);
        entity = fatturaRepository.save(entity);
        return fatturaMapper.toResponse(entity);
    }

    // GET BY ID
    @Override
    public FatturaResponse getById(Long id) throws Throwable {
        Fattura entity = fatturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fattura non trovata"));
        return fatturaMapper.toResponse(entity);
    }

    // GET BY PRODOTTO (paginated)
    @Override
    public PageImpl<FatturaResponse> getByProdotto(Long idProdotto, int page, int size) {
        if (!prodottoRepository.existsById(idProdotto)) {
            throw new ResourceNotFoundException("Prodotto non trovato");
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "dataFattura")
        );

        Page<Fattura> fatture = fatturaRepository.search(
                null,           // numero
                idProdotto,     // idProdotto
                null,           // dataFrom
                null,           // dataTo
                null,           // importoMin
                null,           // importoMax
                pageable
        );

        List<FatturaResponse> content = fatture.getContent()
                .stream()
                .map(fatturaMapper::toResponse)
                .toList();

        return new PageImpl<>(content, pageable, fatture.getTotalElements());
    }

    // DELETE
    @Override
    public void delete(Long id) {
        if (!fatturaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Fattura non trovata");
        }
        fatturaRepository.deleteById(id);
    }
}
