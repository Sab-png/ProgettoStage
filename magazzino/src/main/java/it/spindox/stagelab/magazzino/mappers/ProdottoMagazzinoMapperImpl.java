package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.ProdottoMagazzino.ProdottoMagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.ProdottoMagazzino.ProdottoMagazzinoResponse;
import it.spindox.stagelab.magazzino.entities.ProdottoMagazzino;
import org.springframework.stereotype.Component;

@Component
public class ProdottoMagazzinoMapperImpl implements ProdottoMagazzinoMapper {

    @Override
    public ProdottoMagazzino toEntity(ProdottoMagazzinoRequest request) {
        if (request == null) return null;

        ProdottoMagazzino pm = new ProdottoMagazzino();
        pm.setQuantita(request.getQuantita());
        pm.setScortaMin(request.getScortaMin());

        // prodotto e magazzino vengono settati nel service
        return pm;
    }

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