
package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;
import it.spindox.stagelab.magazzino.entities.Fattura;
import it.spindox.stagelab.magazzino.entities.Prodotto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;




@Slf4j
@Component
public class FatturaMapperImpl implements FatturaMapper {

    /**
     * Converte un DTO FatturaRequest in una entity Fattura.
     *
     * Viene usato in:
     *  - create()
     *
     * NOTA:
     *  - L'ID della fattura non viene impostato (lo genera il DB)
     *  - Il numero fattura viene aggiunto dal servizio tramite sequence
     */
    @Override
    public Fattura toEntity(FatturaRequest request, Prodotto prodotto) {
        Fattura f = new Fattura();
        f.setDataFattura(request.getDataFattura());
        f.setImporto(request.getImporto());
        f.setQuantita(request.getQuantita());
        f.setProdotto(prodotto); // riferimento al prodotto già validato
        return f;
    }

    /**
     * Converte una entity Fattura in un DTO FatturaResponse.
     *
     * Viene usato ovunque la Fattura debba essere restituita al client:
     *  - GET /fatture/{id}
     *  - GET /fatture/list
     *  - POST /fatture/search
     *  - UPDATE
     *
     * NOTA:
     *  - Protegge da NullPointer per entity.getProdotto()
     */
    @Override
    public FatturaResponse toResponse(Fattura entity) {
        FatturaResponse r = new FatturaResponse();
        r.setId(entity.getId());
        r.setNumero(entity.getNumero());
        r.setDataFattura(entity.getDataFattura());
        r.setImporto(entity.getImporto());
        r.setQuantita(entity.getQuantita());

        // Evita NPE: il prodotto potrebbe essere null in casi rari (entity non conforme)
        if (entity.getProdotto() != null) {
            r.setIdProdotto(entity.getProdotto().getId());
        }

        return r;
    }

    /**
     * Aggiorna una entity esistente seguendo la logica "PATCH":
     * vengono modificati solo i campi NON NULL presenti nella request.
     *
     * Questo metodo viene chiamato da:
     *  - update()
     *
     * @param target   entity già salvata nel DB da aggiornare
     * @param request  DTO contenente solo i campi aggiornati
     * @param prodotto (opzionale) nuovo prodotto associato, se fornito nella request
     */
    @Override
    public void updateEntity(Fattura target, FatturaRequest request, Prodotto prodotto) {

        // Aggiorna data fattura se presente nella request
        if (request.getDataFattura() != null) {
            target.setDataFattura(request.getDataFattura());
        }

        // Aggiorna importo se presente
        if (request.getImporto() != null) {
            target.setImporto(request.getImporto());
        }

        // Aggiorna quantità se presente
        if (request.getQuantita() != null) {
            target.setQuantita(request.getQuantita());
        }

        // Aggiorna relazione Prodotto -> Fattura se fornito
        if (prodotto != null) {
            target.setProdotto(prodotto);
        }
    }
}