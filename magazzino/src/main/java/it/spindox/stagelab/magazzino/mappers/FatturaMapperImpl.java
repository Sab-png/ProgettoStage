
package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;
import it.spindox.stagelab.magazzino.entities.Fattura;
import it.spindox.stagelab.magazzino.entities.Prodotto;
import it.spindox.stagelab.magazzino.entities.SXFatturaStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component

public class FatturaMapperImpl implements FatturaMapper {


    // CREATE: DTO : Entity

    @Override

    public Fattura toEntity(FatturaRequest request, Prodotto prodotto) {

        Fattura f = new Fattura();
        f.setDataFattura(request.getDataFattura());
        f.setDataScadenza(request.getDataScadenza());
        f.setImporto(request.getImporto());
        f.setQuantita(request.getQuantita());
        f.setProdotto(prodotto);


        // in CREATE è sempre non pagata
        SXFatturaStatus status = SXFatturaStatus.fromDati(
                request.getImporto(),
                request.getDataScadenza()
        );

        f.setStatus(status);

        return f;
    }


    // Entity : DTO RESPONSE

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

        // Stato fattura

        r.setStatus(entity.getStatus());
        r.setStatusDescription(entity.getStatus().getDescription());

        return r;
    }


    // UPDATE (PATCH)

    @Override

    public void updateEntity(Fattura target, FatturaRequest request, Prodotto prodotto) {

        if (request.getDataFattura() != null) {
            target.setDataFattura(request.getDataFattura());
        }

        if (request.getDataScadenza() != null) {
            target.setDataScadenza(request.getDataScadenza());
        }

        if (request.getImporto() != null) {
            target.setImporto(request.getImporto());
        }

        if (request.getQuantita() != null) {
            target.setQuantita(request.getQuantita());
        }

        if (prodotto != null) {
            target.setProdotto(prodotto);
        }

        // Ricalcola lo stato dopo l’aggiornamento

        SXFatturaStatus status = SXFatturaStatus.fromDati(
                target.getImporto().doubleValue(),
                target.getPagato().doubleValue(),
                target.getDataScadenza()
        );
        target.setStatus(status);
    }
}