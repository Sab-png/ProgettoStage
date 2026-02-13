package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.prodottomagazzino.ProdottoMagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.prodottomagazzino.ProdottoMagazzinoResponse;
import it.spindox.stagelab.magazzino.entities.ProdottoMagazzino;
import it.spindox.stagelab.magazzino.entities.StockStatusProdotto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class ProdottoMagazzinoMapperImpl implements ProdottoMagazzinoMapper {

    @Override
    public ProdottoMagazzino toEntity(ProdottoMagazzinoRequest request) {
        if (request == null) return null;

        ProdottoMagazzino pm = new ProdottoMagazzino();
        pm.setQuantita(request.getQuantita());
        pm.setScortaMin(request.getScortaMin());
        return pm;
    }

    @Override
    public void updateEntity(ProdottoMagazzino entity, ProdottoMagazzinoRequest request) {
        if (entity == null || request == null) return;

        if (request.getQuantita() != null) {
            entity.setQuantita(request.getQuantita());
        }
        if (request.getScortaMin() != null) {
            entity.setScortaMin(request.getScortaMin());
        }
    }

    @Override
    public ProdottoMagazzinoResponse toResponse(ProdottoMagazzino entity) {
        if (entity == null) return null;

        StockStatusProdotto status = StockStatusProdotto.fromQuantita(
                entity.getQuantita(),
                entity.getScortaMin()
        );

        return ProdottoMagazzinoResponse.builder()
                .id(entity.getId())
                .prodottoId(entity.getProdotto() != null ? entity.getProdotto().getId() : null)
                .nomeProdotto(entity.getProdotto() != null ? entity.getProdotto().getNome() : null)
                .magazzinoId(entity.getMagazzino() != null ? entity.getMagazzino().getId() : null)
                .nomeMagazzino(entity.getMagazzino() != null ? entity.getMagazzino().getNome() : null)
                .quantita(entity.getQuantita())
                .scortaMin(entity.getScortaMin())
                .status(status)
                .statusColor(status.getDbValue())
                .statusDescription(status.getDescription())
                .build();
    }
}