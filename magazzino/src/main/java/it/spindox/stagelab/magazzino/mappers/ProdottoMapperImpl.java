
package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.prodotto.ProdottoRequest;
import it.spindox.stagelab.magazzino.dto.prodotto.ProdottoResponse;
import it.spindox.stagelab.magazzino.entities.Prodotto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;



//  Converte un DTO ProdottoRequest : entity Prodotto
//   • Usato in: create()
//   • L'ID NON viene impostato (lo genera il DB)
//   • Prezzo è BigDecimal :assegnazione diretta se presente
//   • request può essere null : ritorna null




@Slf4j
@Component

public class ProdottoMapperImpl implements ProdottoMapper {

    @Override

    public Prodotto toEntity(ProdottoRequest request) {
        if (request == null) {
            return null;
        }

        Prodotto p = new Prodotto();
        p.setNome(request.getNome());
        p.setDescrizione(request.getDescrizione());

        // Prezzo BigDecimal: copia diretta
        if (request.getPrezzo() != null) {
            p.setPrezzo(request.getPrezzo());
        }

        return p;
    }

// RESPONSE

    @Override

    public ProdottoResponse toResponse(Prodotto entity) {
        if (entity == null) {
            return null;
        }

        ProdottoResponse r = new ProdottoResponse();
        r.setId(entity.getId());
        r.setNome(entity.getNome());
        r.setDescrizione(entity.getDescrizione());
        r.setPrezzo(entity.getPrezzo()); // BigDecimal

        return r;
    }

// UPDATE

    @Override

    public void updateEntity(Prodotto p, ProdottoRequest request) {
        if (p == null || request == null) {
            return;
        }

        if (request.getNome() != null) {
            p.setNome(request.getNome());
        }
        if (request.getDescrizione() != null) {
            p.setDescrizione(request.getDescrizione());
        }
        if (request.getPrezzo() != null) {
            p.setPrezzo(request.getPrezzo());
        }
    }
}