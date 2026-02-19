
package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaSearchRequest;
import it.spindox.stagelab.magazzino.entities.Fattura;
import it.spindox.stagelab.magazzino.entities.Prodotto;
import it.spindox.stagelab.magazzino.entities.SXFatturaStatus;
import it.spindox.stagelab.magazzino.exceptions.ResourceNotFoundException;
import it.spindox.stagelab.magazzino.exceptions.fatturaexceptions.InvalidDataFatturaException;
import it.spindox.stagelab.magazzino.exceptions.fatturaexceptions.InvalidImportoFatturaException;
import it.spindox.stagelab.magazzino.mappers.FatturaMapper;
import it.spindox.stagelab.magazzino.repositories.FatturaRepository;
import it.spindox.stagelab.magazzino.repositories.ProdottoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.List;



@Slf4j
@Service
@RequiredArgsConstructor
public class FatturaServiceImpl implements FatturaService {

    private final FatturaRepository fatturaRepository;
    private final ProdottoRepository prodottoRepository;
    private final FatturaMapper fatturaMapper;

    @Override
    public Page<FatturaResponse> search(FatturaSearchRequest request) {
        log.info("Ricerca fatture avviata");
        if (request == null) {
            log.warn("FatturaSearchRequest è NULL: ritorno pagina vuota");
            return Page.empty();
        }

        log.debug("Parametri ricerca: numero='{}', idProdotto={}, dataFrom={}, dataTo={}, importoMin={}, importoMax={}, page={}, size={}",
                request.getNumero(),
                request.getIdProdotto(),
                request.getDataFrom(),
                request.getDataTo(),
                request.getImportoMin(),
                request.getImportoMax(),
                request.getPage(),
                request.getSize()
        );

        return null;
    }

    // CREATE

    @Override
    public FatturaResponse create(FatturaRequest request) {
        if (request.getIdProdotto() == null) {
            throw new InvalidImportoFatturaException(null, request.getImporto(), request.getQuantita());
        }

        if (request.getDataScadenza() != null &&
                request.getDataFattura() != null &&
                request.getDataScadenza().isBefore(request.getDataFattura())) {

            throw new InvalidDataFatturaException(
                    null,
                    request.getDataFattura(),
                    request.getDataScadenza()
            );
        }

        Prodotto prodotto = prodottoRepository.findById(request.getIdProdotto())
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato"));

        Fattura entity = fatturaMapper.toEntity(request, prodotto);

        Long nextVal = fatturaRepository.nextNumeroSeq();
        entity.setNumero("FAT-" + nextVal);

        entity = fatturaRepository.save(entity);

        return fatturaMapper.toResponse(entity);
    }

    // UPDATE (PATCH)

    @Override
    public FatturaResponse update(Long id, FatturaRequest request) {
        Fattura entity = fatturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fattura non trovata"));

        if (entity.getStatus() == SXFatturaStatus.PAGATA) {
            throw new FatturaAlreadyPaidException(id);
        }

        if (request.getDataFattura() != null &&
                request.getDataScadenza() != null &&
                request.getDataScadenza().isBefore(request.getDataFattura())) {

            throw new InvalidDataFatturaException(
                    id,
                    request.getDataFattura(),
                    request.getDataScadenza()
            );
        }

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
    public FatturaResponse getById(Long id) {
        Fattura entity = fatturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fattura non trovata"));
        return fatturaMapper.toResponse(entity);
    }

// GET BY PRODOTTO

    @Override
    public PageImpl<FatturaResponse> getByProdotto(Long idProdotto, int page, int size) {
        log.info("Recupero fatture per prodotto: idProdotto={}, page={}, size={}", idProdotto, page, size);

        if (idProdotto == null) {
            log.warn("idProdotto è NULL in getByProdotto: ritorno pagina vuota");
            return new PageImpl<>(List.of(), PageRequest.of(page, size), 0);
        }

        log.debug("Costruzione PageRequest page={}, size={}", page, size);
        return null;
    }

    @Override
    public void delete(Long id) {
        log.info("Richiesta cancellazione fattura id={}", id);

        if (id == null) {
            log.warn("ID NULL passato a delete: nessuna operazione eseguita");
        }

    }

    @Override
    public Page<Long> searchIds(FatturaSearchRequest req) {
        log.info("Ricerca ID fatture avviata");
        if (req == null) {
            log.warn("FatturaSearchRequest è NULL in searchIds: ritorno pagina vuota");
            return Page.empty();
        }

        log.debug("Parametri ricerca ID: numero='{}', idProdotto={}, dataFrom={}, dataTo={}, importoMin={}, importoMax={}, page={}, size={}",
                req.getNumero(),
                req.getIdProdotto(),
                req.getDataFrom(),
                req.getDataTo(),
                req.getImportoMin(),
                req.getImportoMax(),
                req.getPage(),
                req.getSize()
        );
        return null;
    }

    @Override
    public Page<FatturaResponse> getAllPaged(int page, int size) {
        log.info("Recupero paginato di tutte le fatture: page={}, size={}", page, size);
        PageRequest pr = PageRequest.of(page, size);
        log.debug("PageRequest creato: {}", pr);
        return null;
    }
}