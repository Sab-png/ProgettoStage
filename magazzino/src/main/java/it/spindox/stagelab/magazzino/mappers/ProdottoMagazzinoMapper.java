package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.prodottomagazzino.ProdottoMagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.prodottomagazzino.ProdottoMagazzinoResponse;
import it.spindox.stagelab.magazzino.entities.ProdottoMagazzino;

public interface ProdottoMagazzinoMapper {

    ProdottoMagazzino toEntity(ProdottoMagazzinoRequest request);

    void updateEntity(ProdottoMagazzino entity, ProdottoMagazzinoRequest request);

    ProdottoMagazzinoResponse toResponse(ProdottoMagazzino entity);
}