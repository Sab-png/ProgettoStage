package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaSearchRequest;
import it.spindox.stagelab.magazzino.entities.Fattura;
import it.spindox.stagelab.magazzino.entities.Prodotto;
import it.spindox.stagelab.magazzino.entities.SXFatturaStatus;
import it.spindox.stagelab.magazzino.exceptions.ResourceNotFoundException;
import it.spindox.stagelab.magazzino.exceptions.jobsexceptions.InvalidFatturaException;
import it.spindox.stagelab.magazzino.mappers.FatturaMapper;
import it.spindox.stagelab.magazzino.repositories.FatturaRepository;
import it.spindox.stagelab.magazzino.repositories.ProdottoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import static it.spindox.stagelab.magazzino.entities.SXFatturaStatus.determine;




@Slf4j
@Service
@RequiredArgsConstructor
@Transactional

public  class FatturaServiceImpl implements FatturaService {

    private final FatturaRepository fatturaRepository;
    private final ProdottoRepository prodottoRepository;
    private final FatturaMapper fatturaMapper;


    @PersistenceContext
    private EntityManager entityManager;



    //  SEARCH: ricerca fatture

    @Override
    @Transactional(readOnly = true)
    public Page<FatturaResponse> search(FatturaSearchRequest request) {

        if (request == null) return Page.empty();

        int page = Math.max(request.getPage(), 0);
        int size = Math.max(request.getSize(), 1);

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        Fattura probe = new Fattura();

        if (request.getNumero() != null && !request.getNumero().isBlank())
            probe.setNumero(request.getNumero());

        if (request.getIdProdotto() != null) {
            Prodotto p = new Prodotto();
            p.setId(request.getIdProdotto());
            probe.setProdotto(p);
        }

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreCase()
                .withMatcher("numero", ExampleMatcher.GenericPropertyMatchers.contains());

        Page<Fattura> result =
                fatturaRepository.findAll(Example.of(probe, matcher), pageable);

        return result.map(fatturaMapper::toResponse);
    }



    //  SEARCH SOLO ID

    @Override
    @Transactional(readOnly = true)
    public Page<Long> searchIds(FatturaSearchRequest req) {
        Page<FatturaResponse> pageResp = search(req);
        return pageResp.map(FatturaResponse::getId);
    }


    //  CREATE FATTURA

    @Override
    public FatturaResponse create(FatturaRequest request) {

        if (request == null)
            throw new IllegalArgumentException("FatturaRequest è null");

        if (request.getIdProdotto() == null)
            throw new InvalidFatturaException(null, request.getImporto(), request.getQuantita());

        if (request.getDataScadenza() != null &&
                request.getDataFattura() != null &&
                request.getDataScadenza().isBefore(request.getDataFattura())) {
            throw new InvalidFatturaException((Long) null);
        }

        Prodotto prodotto = prodottoRepository
                .findById(request.getIdProdotto())
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato"));

        Fattura entity = fatturaMapper.toEntity(request, prodotto);

        Long seq = fatturaRepository.nextNumeroSeq();
        entity.setNumero("FAT-" + seq);

        entity.setPagato(BigDecimal.ZERO);

        entity.setStatus(determine(
                entity.getImporto(),
                entity.getPagato(),
                entity.getDataScadenza()
        ));

        entity = fatturaRepository.save(entity);
        return fatturaMapper.toResponse(entity);
    }


    //  UPDATE (PATCH)

    @Override
    public FatturaResponse update(Long id, FatturaRequest request) {

        Fattura entity = fatturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fattura non trovata"));

        if (entity.getStatus() == SXFatturaStatus.PAGATA)
            throw new InvalidFatturaException("La fattura è già pagata e non può essere modificata");

        if (request.getDataFattura() != null &&
                request.getDataScadenza() != null &&
                request.getDataScadenza().isBefore(request.getDataFattura())) {
            throw new InvalidFatturaException(id);
        }

        Prodotto prodotto = null;
        if (request.getIdProdotto() != null) {
            prodotto = prodottoRepository.findById(request.getIdProdotto())
                    .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato"));
        }

        fatturaMapper.updateEntity(entity, request, prodotto);

        entity.setStatus(determine(
                entity.getImporto(),
                entity.getPagato(),
                entity.getDataScadenza()
        ));

        entity = fatturaRepository.save(entity);
        return fatturaMapper.toResponse(entity);
    }



    //  GET BY ID

    @Override
    @Transactional(readOnly = true)
    public FatturaResponse getById(Long id) {
        Fattura entity = fatturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fattura non trovata"));
        return fatturaMapper.toResponse(entity);
    }


    //  GET BY PRODOTTO

    @Override
    @Transactional(readOnly = true)
    public PageImpl<FatturaResponse> getByProdotto(Long idProdotto, int page, int size) {

        if (idProdotto == null)
            return new PageImpl<>(List.of(), PageRequest.of(page, size), 0);

        List<Fattura> list = fatturaRepository.findByProdottoId(idProdotto);

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), list.size());

        List<FatturaResponse> content =
                list.subList(start, end).stream()
                        .map(fatturaMapper::toResponse)
                        .toList();

        return new PageImpl<>(content, pageable, list.size());
    }



    //  DELETE

    @Override
    public void delete(Long id) {

        if (id == null) return;

        Fattura entity = fatturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fattura non trovata: id=" + id));

        fatturaRepository.delete(entity);
    }



    //  GET ALL PAGED

    @Override
    @Transactional(readOnly = true)
    public Page<FatturaResponse> getAllPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return fatturaRepository.findAll(pageable).map(fatturaMapper::toResponse);
    }

}