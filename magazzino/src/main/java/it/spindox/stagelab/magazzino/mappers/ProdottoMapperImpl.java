
package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.prodotto.ProdottoRequest;
import it.spindox.stagelab.magazzino.dto.prodotto.ProdottoResponse;
import it.spindox.stagelab.magazzino.entities.Prodotto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;




@Slf4j
@Component
public class ProdottoMapperImpl implements ProdottoMapper {


      // Converte un DTO ProdottoRequest in entity Prodotto.
     // Viene usato in: create()

     // L'ID è generato dal DB di default
     // Prezzo è BigDecimal → assegnazione diretta se presente

    @Override
    public Prodotto toEntity(ProdottoRequest request) {
        if (request == null) {
            return null;
        }

        Prodotto p = new Prodotto();
        p.setNome(request.getNome());
        p.setDescrizione(request.getDescrizione());

        // request.getPrezzo() è già BigDecimal :  assegnazione diretta se non null

        if (request.getPrezzo() != null) {
            p.setPrezzo(request.getPrezzo());
        }

        return p;
    }


     // Converte una entity Prodotto in DTO ProdottoResponse

      // Viene usato quando si restituisce il prodotto al client:

      // GET /prodotti/{id}
     // GET /prodotti/list
      // POST /prodotti/search

     // Protegge da null su entity
     // Prezzo (BigDecimal) viene passato così com'è

    @Override
    public ProdottoResponse toResponse(Prodotto entity) {
        if (entity == null) {
            return null;
        }

        ProdottoResponse r = new ProdottoResponse();
        r.setId(entity.getId());
        r.setNome(entity.getNome());
        r.setDescrizione(entity.getDescrizione());
        r.setPrezzo(entity.getPrezzo()); // BigDecimal → pass-through

        return r;
    }


     // Aggiorna una entity esistente in modalità "PATCH":
     // modifica solo i campi NON NULL presenti nella request.
     // Viene usato in: update()


    // Prezzo è BigDecimal :  assegnazione diretta se non null


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
            p.setPrezzo(request.getPrezzo()); // BigDecimal → assegnazione diretta
        }
    }
}