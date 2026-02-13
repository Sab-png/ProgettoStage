
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;



@Slf4j
@Service
@RequiredArgsConstructor
public class FatturaServiceImpl implements FatturaService {

    // Repository per salvare/leggere le fatture

    private final FatturaRepository fatturaRepository;

    // Repository per validare ed associare un prodotto esistente

    private final ProdottoRepository prodottoRepository;

    // Mapper che converte Entity <-> DTO

    private final FatturaMapper fatturaMapper;


    //  GET ALL PAGED + STREAM
    //  Usato da GET /fatture/list

    @Override
    @Transactional(readOnly = true) // Solo lettura -> più efficiente
    public Page<FatturaResponse> getAllPaged(int page, int size) {

        // Costruisce un Pageable, proteggendo da page < 0 o size <= 0
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.max(size, 1),
                Sort.by(Sort.Direction.DESC, "dataFattura") // Ordina per data
        );

        // Recupera page di Fattura dal DB
        Page<Fattura> fatturePage = fatturaRepository.findAll(pageable);

        // STREAM:
        // Mappa ogni entity Fattura -> FatturaResponse DTO

        List<FatturaResponse> content = fatturePage.getContent()
                .stream()
                .map(fatturaMapper::toResponse)
                .toList();

        // Ritorna una PageImpl mantenendo numero pagina / totale elementi
        return new PageImpl<>(content, pageable, fatturePage.getTotalElements());
    }


    //  SEARCH COMPLETA (Page di DTO)
    //  Usato da POST /fatture/search

    @Override
    public Page<FatturaResponse> search(FatturaSearchRequest request) {

        // Pageable con page/size e ordinamento

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by(Sort.Direction.DESC, "dataFattura")
        );

        // Query JPQL custom definita nella repository

        Page<Fattura> page = fatturaRepository.search(
                request.getNumero(),
                request.getIdProdotto(),
                request.getDataFrom(),
                request.getDataTo(),
                request.getImportoMin(),
                request.getImportoMax(),
                pageable
        );

        // Page.map() converte automaticamente entity -> DTO

        return page.map(fatturaMapper::toResponse);
    }


    //  CREATE
    //  Valida prodotto, genera numero fattura da SEQUENCE, salva

    @Override
    public FatturaResponse create(FatturaRequest request) {

        // Deve esserci un prodotto per la fattura

        if (request.getIdProdotto() == null) {
            throw new IllegalArgumentException("Id prodotto obbligatorio");
        }

        // Carica il prodotto o lancia 404

        Prodotto prodotto = prodottoRepository.findById(request.getIdProdotto())
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato"));

        // Converte DTO -> Entity

        Fattura entity = fatturaMapper.toEntity(request, prodotto);

        // Genera numero fattura: es. FAT-12

        Long nextVal = fatturaRepository.nextNumeroSeq();
        entity.setNumero("FAT-" + nextVal);

        // Salva su DB

        entity = fatturaRepository.save(entity);

        // Ritorna DTO

        return fatturaMapper.toResponse(entity);
    }



    //  UPDATE (PATCH)
    //  Aggiorna solo campi presenti nel DTO

    @Override
    public FatturaResponse update(Long id, FatturaRequest request) throws Throwable {

        // Carica la fattura da aggiornare

        Fattura entity = fatturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fattura non trovata"));

        // Carica nuovo prodotto se richiesto

        Prodotto prodotto = null;
        if (request.getIdProdotto() != null) {
            prodotto = prodottoRepository.findById(request.getIdProdotto())
                    .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato"));
        }

        // Mapper applica aggiornamenti SOLO sui campi non null

        fatturaMapper.updateEntity(entity, request, prodotto);

        // Salva

        entity = fatturaRepository.save(entity);

        return fatturaMapper.toResponse(entity);
    }



    //  GET BY ID

    @Override
    public FatturaResponse getById(Long id) throws Throwable {

        // Cerca oppure errore 404
        Fattura entity = fatturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fattura non trovata"));

        return fatturaMapper.toResponse(entity);
    }



    //  GET BY PRODOTTO (Lista paginata)

    @Override
    public PageImpl<FatturaResponse> getByProdotto(Long idProdotto, int page, int size) {

        // Verifica che il prodotto esista
        if (!prodottoRepository.existsById(idProdotto)) {
            throw new ResourceNotFoundException("Prodotto non trovato");
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "dataFattura")
        );

        // Riutilizza query di ricerca fatture filtrate per prodotto
        Page<Fattura> fatture = fatturaRepository.search(
                null, idProdotto, null, null, null, null, pageable
        );

        // STREAM: conversione in DTO
        List<FatturaResponse> content = fatture.getContent()
                .stream()
                .map(fatturaMapper::toResponse)
                .toList();

        // Ricompone la PageImpl
        return new PageImpl<>(content, pageable, fatture.getTotalElements());
    }


    //  DELETE

    @Override
    public void delete(Long id) {

        // Se la fattura non esiste -> errore
        if (!fatturaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Fattura non trovata");
        }

        // Cancellazione
        fatturaRepository.deleteById(id);
    }



    //  GET ALL SOLO ID (usato dal GET /fatture)

    @Override
    @Transactional(readOnly = true)
    public Page<Long> searchIds(FatturaSearchRequest request) {

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize()
        );

        // Query JPQL del repository che ritorna SOLO gli ID

        return fatturaRepository.searchIds(
                request.getNumero(),
                request.getIdProdotto(),
                request.getDataFrom(),
                request.getDataTo(),
                request.getImportoMin(),
                request.getImportoMax(),
                pageable
        );
    }
}