
package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;
import it.spindox.stagelab.magazzino.entities.Fattura;
import it.spindox.stagelab.magazzino.entities.Prodotto;
import it.spindox.stagelab.magazzino.entities.SXFatturaStatus;
import org.springframework.stereotype.Component;




@Component
public class FatturaMapperImpl implements FatturaMapper {

    @Override
    public Fattura toEntity(FatturaRequest request, Prodotto prodotto) {
        Fattura f = new Fattura();
        f.setUsername(request.getUsername());
        f.setDataFattura(request.getDataFattura());
        f.setDataScadenza(request.getDataScadenza());
        f.setImporto(request.getImporto());
        f.setQuantita(request.getQuantita());
        f.setProdotto(prodotto);

        return f;
    }
// DTO DI RESPONSE CON TUTTI I CAMPI DELL'ENTITA' E ID PRODOTTO (SE NON NULL) E DESCRIZIONE DELLO STATUS

    @Override
    public FatturaResponse toResponse(Fattura entity) {
        FatturaResponse r = new FatturaResponse();

        r.setId(entity.getId());
        r.setNumero(entity.getNumero());
        r.setDataFattura(entity.getDataFattura());
        r.setDataScadenza(entity.getDataScadenza());
        r.setImporto(entity.getImporto());
        r.setQuantita(entity.getQuantita());
        r.setPagato(entity.getPagato());

        if (entity.getProdotto() != null) {
            r.setIdProdotto(entity.getProdotto().getId());
        }

        r.setStatus(entity.getStatus());
        r.setStatusDescription(entity.getStatus() != null
                ? entity.getStatus().getDescription()
                : null
        );

        return r;
    }
// UPDATE PARZIALE DELL'ENTITA' CON I CAMPI NON NULL DEL REQUEST E PRODOTTO (SE NON NULL)
    @Override
    public void updateEntity(Fattura target, FatturaRequest request, Prodotto prodotto) {

        if (request.getDataFattura() != null)
            target.setDataFattura(request.getDataFattura());

        if (request.getDataScadenza() != null)
            target.setDataScadenza(request.getDataScadenza());

        if (request.getImporto() != null)
            target.setImporto(request.getImporto());

        if (request.getQuantita() != null)
            target.setQuantita(request.getQuantita());

        if (prodotto != null)
            target.setProdotto(prodotto);

        // Ricalcolo stato SOLO se la fattura NON è pagata

        if (target.getStatus() != SXFatturaStatus.PAGATA) {
            SXFatturaStatus nuovo = SXFatturaStatus.determine(
                    target.getImporto(),
                    target.getPagato(),
                    target.getDataScadenza()
            );
            target.setStatus(nuovo);
        }
    }
}
