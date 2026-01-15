package it.spindox.stagelab.magazzino.services;

import it.spindox.stagelab.magazzino.dto.request.FatturaRequest;
import it.spindox.stagelab.magazzino.dto.request.FatturaSearchRequest;
import it.spindox.stagelab.magazzino.dto.response.FatturaResponse;
import it.spindox.stagelab.magazzino.entities.Fattura;
import it.spindox.stagelab.magazzino.entities.Prodotto;
import it.spindox.stagelab.magazzino.mappers.FatturaMapper;
import it.spindox.stagelab.magazzino.repositories.FatturaRepository;
import it.spindox.stagelab.magazzino.repositories.ProdottoRepository;
import it.spindox.stagelab.magazzino.services.FatturaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FatturaServiceImpl implements FatturaService {

    private final FatturaRepository fatturaRepository;
    private final ProdottoRepository prodottoRepository;

    public FatturaServiceImpl(FatturaRepository fatturaRepository,
                              ProdottoRepository prodottoRepository) {
        this.fatturaRepository = fatturaRepository;
        this.prodottoRepository = prodottoRepository;
    }

    @Override
    public FatturaResponse getFattura(Long id) {
        return fatturaRepository.findById(id)
                .map(FatturaMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Fattura non trovata"));
    }

    @Override
    public List<FatturaResponse> getFattureByProdotto(Long prodottoId) {
        return fatturaRepository.findByProdottoId(prodottoId)
                .stream()
                .map(FatturaMapper::toResponse)
                .toList();
    }

    @Override
    public FatturaResponse saveFattura(FatturaRequest request) {
        Prodotto prodotto = prodottoRepository.findById(request.getProdottoId())
                .orElseThrow(() -> new EntityNotFoundException("Prodotto non trovato"));

        Fattura fattura = new Fattura();
        fattura.setDataFattura(request.getDataFattura());
        fattura.setImporto(request.getImporto());
        fattura.setProdotto(prodotto);

        return FatturaMapper.toResponse(fatturaRepository.save(fattura));
    }

    @Override
    public FatturaResponse editFattura(Long id, FatturaRequest request) {
        Fattura fattura = fatturaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fattura non trovata"));

        if (request.getDataFattura() != null)
            fattura.setDataFattura(request.getDataFattura());
        if (request.getImporto() != null)
            fattura.setImporto(request.getImporto());

        return FatturaMapper.toResponse(fatturaRepository.save(fattura));
    }

    @Override
    public Page<FatturaResponse> searchFattura(FatturaSearchRequest request) {
        return fatturaRepository.search(
                        request.getDataDa(),
                        request.getDataA(),
                        request.getImportoMin(),
                        request.getImportoMax(),
                        PageRequest.of(request.getPage(), request.getSize())
                )
                .map(FatturaMapper::toResponse);
    }
}
