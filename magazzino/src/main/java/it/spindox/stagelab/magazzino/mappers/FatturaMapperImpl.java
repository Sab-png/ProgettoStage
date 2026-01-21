
package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;
import it.spindox.stagelab.magazzino.entities.Fattura;
import it.spindox.stagelab.magazzino.entities.Prodotto;
import it.spindox.stagelab.magazzino.repositories.ProdottoRepository;
import org.springframework.stereotype.Component;

/**
 * Implementazione manuale del mapper Fattura.
 */
@Component
public class FatturaMapperImpl implements FatturaMapper {

    private final ProdottoRepository prodottoRepository;

    public FatturaMapperImpl(ProdottoRepository prodottoRepository) {
        this.prodottoRepository = prodottoRepository;
    }

    /**
     * CREATE: FatturaRequest -> Fattura
     */
    @Override
    public Fattura toEntity(FatturaRequest request) {
        if (request == null) {
            return null;
        }

        Fattura fattura = new Fattura();
        fattura.setNumero(request.getNumero());
        fattura.setDataFattura(request.getData());
        fattura.setImporto(request.getImporto());
        fattura.setQuantita(request.getQuantita());

        if (request.getIdProdotto() != null) {
            Prodotto prodotto = prodottoRepository.findById(request.getIdProdotto())
                    .orElseThrow(() ->
                            new IllegalArgumentException("Prodotto non trovato: " + request.getIdProdotto())
                    );
            fattura.setProdotto(prodotto);
        }

        return fattura;
    }

    /**
     * READ: Fattura -> FatturaResponse
     */
    @Override
    public FatturaResponse toResponse(Fattura entity) {
        if (entity == null) {
            return null;
        }

        FatturaResponse response = new FatturaResponse();
        response.setId(entity.getId());
        response.setNumero(entity.getNumero());
        response.setDataFattura(entity.getDataFattura());
        response.setImporto(entity.getImporto());
        response.setQuantita(entity.getQuantita());

        if (entity.getProdotto() != null) {
            response.setIdProdotto(entity.getProdotto().getId());
        }

        return response;
    }

    /**
     * UPDATE: aggiorna solo i campi NON null
     */
    @Override
    public void updateEntity(Fattura target, FatturaRequest request) {
        if (target == null || request == null) {
            return;
        }

        if (request.getNumero() != null) {
            target.setNumero(request.getNumero());
        }

        if (request.getData() != null) {
            target.setDataFattura(request.getData());
        }

        if (request.getImporto() != null) {
            target.setImporto(request.getImporto());
        }

        if (request.getQuantita() != null) {
            target.setQuantita(request.getQuantita());
        }

        if (request.getIdProdotto() != null) {
            Prodotto prodotto = prodottoRepository.findById(request.getIdProdotto())
                    .orElseThrow(() ->
                            new IllegalArgumentException("Prodotto non trovato: " + request.getIdProdotto())
                    );
            target.setProdotto(prodotto);
        }
    }
}
