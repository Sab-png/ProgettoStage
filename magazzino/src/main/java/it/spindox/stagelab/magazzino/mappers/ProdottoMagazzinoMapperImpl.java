package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.ProdottoMagazzino.ProdottoMagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.ProdottoMagazzino.ProdottoMagazzinoResponse;
import it.spindox.stagelab.magazzino.entities.ProdottoMagazzino;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class ProdottoMagazzinoMapperImpl implements ProdottoMagazzinoMapper {

    /**
     * Converte un DTO ProdottoMagazzinoRequest in entity ProdottoMagazzino.
     *
     * Viene usato in:
     *  - create() di stock/associazione prodotto-magazzino
     *
     * NOTE:
     *  - L'ID è generato dal DB
     *  - Il collegamento a Prodotto e Magazzino viene gestito dal service (validazione e fetch)
     */
    @Override
    public ProdottoMagazzino toEntity(ProdottoMagazzinoRequest request) {
        if (request == null) return null;

        ProdottoMagazzino pm = new ProdottoMagazzino();
        pm.setQuantita(request.getQuantita());
        pm.setScortaMin(request.getScortaMin());

        // prodotto e magazzino vengono settati nel service
        return pm;
    }

    /**
     * Aggiornamento in modalità "PATCH":
     * modifica solo i campi NON NULL (quantita, scortaMin).
     *
     * Viene usato in:
     *  - update()
     *
     * NOTE:
     *  - Prodotto e Magazzino NON vengono aggiornati qui (rimangono invariati)
     */
    @Override
    public void updateEntity(ProdottoMagazzino entity, ProdottoMagazzinoRequest request) {
        if (entity == null || request == null) return;

        if (request.getQuantita() != null) {
            entity.setQuantita(request.getQuantita());
        }

        if (request.getScortaMin() != null ) {
            entity.setScortaMin(request.getScortaMin());
        }

        // Prodotto e Magazzino NON vengono cambiati qui
    }

    /**
     * Converte l'entity ProdottoMagazzino in DTO ProdottoMagazzinoResponse.
     *
     * Viene usato quando si restituisce l'associazione al client:
     *  - GET /stock/{id}
     *  - GET /stock/list
     *  - POST /stock/search
     *
     * NOTE:
     *  - Protegge da NPE su getProdotto() e getMagazzino()
     *  - Espone sia gli ID che i nomi (se disponibili)
     */
    @Override
    public ProdottoMagazzinoResponse toResponse(ProdottoMagazzino entity) {
        if (entity == null) return null;

        return new ProdottoMagazzinoResponse(
                entity.getId(),
                entity.getProdotto() != null ? entity.getProdotto().getId() : null,
                entity.getProdotto() != null ? entity.getProdotto().getNome() : null,
                entity.getMagazzino() != null ? entity.getMagazzino().getId() : null,
                entity.getMagazzino() != null ? entity.getMagazzino().getNome() : null,
                entity.getQuantita(),
                entity.getScortaMin()
        );
    }
}