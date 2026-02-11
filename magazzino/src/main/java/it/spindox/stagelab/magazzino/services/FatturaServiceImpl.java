
package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaSearchRequest;
import it.spindox.stagelab.magazzino.entities.Fattura;
import it.spindox.stagelab.magazzino.entities.Prodotto;
import it.spindox.stagelab.magazzino.exceptions.ResourceNotFoundException;
import it.spindox.stagelab.magazzino.mappers.FatturaMapper;
import it.spindox.stagelab.magazzino.repositories.FatturaRepository;
import it.spindox.stagelab.magazzino.repositories.ProdottoRepository;
import jakarta.validation.Valid;
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
    public Page<FatturaResponse> search(FatturaSearchRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by(Sort.Direction.DESC, "dataFattura")
        );

        Page<Fattura> page = fatturaRepository.search(
                request.getNumero(),
                request.getIdProdotto(),
                request.getDataFrom(),
                request.getDataTo(),
                request.getImportoMin(), // BigDecimal già tipizzato
                request.getImportoMax(),
                pageable
        );

        return page.map(fatturaMapper::toResponse);
    }

    // CREATE CON SEQUENCE
    @Override
    public FatturaResponse create(FatturaRequest request) {

        if (request.getIdProdotto() == null) {
            throw new IllegalArgumentException("Id prodotto obbligatorio");
        }

        Prodotto prodotto = prodottoRepository.findById(request.getIdProdotto())
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato"));

        Fattura entity = fatturaMapper.toEntity(request, prodotto);

        // GENERAZIONE NUMERO FATTURA DA SEQUENCE
        Long nextVal = fatturaRepository.nextNumeroSeq();
        entity.setNumero("FAT-" + nextVal);

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

    // GET BY PRODOTTO
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
                null, idProdotto, null, null, null, null, pageable
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