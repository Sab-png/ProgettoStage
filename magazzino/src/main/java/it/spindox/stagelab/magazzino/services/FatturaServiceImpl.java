
package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;import it.spindox.stagelab.magazzino.entities.Fattura;
import it.spindox.stagelab.magazzino.entities.Prodotto;
import it.spindox.stagelab.magazzino.repositories.FatturaRepository;
import it.spindox.stagelab.magazzino.repositories.ProdottoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class FatturaServiceImpl implements FatturaService {

    private final FatturaRepository fatturaRepository;
    private final ProdottoRepository prodottoRepository;

    @Override
    public Page<Fattura> search(FatturaRequest request) {

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by("dataFattura").descending()
        );

        return fatturaRepository.search(
                request.getIdProdotto(),
                request.getDataFrom(),
                request.getDataTo(),
                toDouble(request.getImportoMin()),
                toDouble(request.getImportoMax()),
                pageable
        );
    }

    @Override
    public Fattura create(FatturaRequest request) {

        if (request.getNumero() == null) {
            throw new IllegalArgumentException("Numero fattura obbligatorio");
        }

        if (request.getIdProdotto() == null) {
            throw new IllegalArgumentException("Prodotto obbligatorio");
        }

        Prodotto prodotto = prodottoRepository.findById(request.getIdProdotto())
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato"));

        Fattura fattura = new Fattura();
        fattura.setNumero(request.getNumero());
        fattura.setDataFattura(request.getData());
        fattura.setQuantita(request.getQuantita());
        fattura.setImporto(request.getImporto());
        fattura.setProdotto(prodotto);

        return fatturaRepository.save(fattura);
    }

    @Override
    public Fattura update(Long id, FatturaRequest request) {

        Fattura fattura = fatturaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fattura non trovata"));

        if (request.getNumero() != null) {
            fattura.setNumero(request.getNumero());
        }

        if (request.getData() != null) {
            fattura.setDataFattura(request.getData());
        }

        if (request.getQuantita() != null) {
            fattura.setQuantita(request.getQuantita());
        }

        if (request.getImporto() != null) {
            fattura.setImporto(request.getImporto());
        }

        return fatturaRepository.save(fattura);
    }

    @Override
    public Fattura findById(Long id) {
        return fatturaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fattura non trovata"));
    }@Override
    public FatturaResponse getById(Long id) {
        return null;
    }@Override
    public Range getByProdotto(Long idProdotto, int page, int size) {
        return null;
    }@Override
    public void delete(Long id) {

    }

    private Double toDouble(BigDecimal value) {
        return value != null ? value.doubleValue() : null;
    }
}
