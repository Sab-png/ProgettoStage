package it.spindox.stagelab.magazzino.services;

import it.spindox.stagelab.magazzino.dto.request.ProdottoRequest;
import it.spindox.stagelab.magazzino.dto.request.ProdottoSearchRequest;
import it.spindox.stagelab.magazzino.dto.response.ProdottoResponse;
import it.spindox.stagelab.magazzino.entities.Prodotto;
import it.spindox.stagelab.magazzino.mappers.ProdottoMapper;
import it.spindox.stagelab.magazzino.repositories.ProdottoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProdottoServiceImpl implements ProdottoService {

    private final ProdottoRepository repository;

    public ProdottoServiceImpl(ProdottoRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProdottoResponse getProdotto(Long id) {
        Prodotto prodotto = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prodotto non trovato"));

        return ProdottoMapper.toResponse(prodotto);
    }

    @Override
    public ProdottoResponse saveProdotto(ProdottoRequest request) {
        Prodotto prodotto = new Prodotto();
        prodotto.setNome(request.getNome());
        prodotto.setDescrizione(request.getDescrizione());
        prodotto.setPrezzo(request.getPrezzo());

        return ProdottoMapper.toResponse(repository.save(prodotto));
    }

    @Override
    public ProdottoResponse editProdotto(Long id, ProdottoRequest request) {
        Prodotto prodotto = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prodotto non trovato"));

        if (request.getNome() != null)
            prodotto.setNome(request.getNome());
        if (request.getDescrizione() != null)
            prodotto.setDescrizione(request.getDescrizione());
        if (request.getPrezzo() != null)
            prodotto.setPrezzo(request.getPrezzo());

        return ProdottoMapper.toResponse(repository.save(prodotto));
    }

    @Override
    public Page<ProdottoResponse> searchProdotto(ProdottoSearchRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        return repository.search(
                request.getNome(),
                request.getPrezzoMin(),
                request.getPrezzoMax(),
                pageable
        ).map(ProdottoMapper::toResponse);
    }
}

