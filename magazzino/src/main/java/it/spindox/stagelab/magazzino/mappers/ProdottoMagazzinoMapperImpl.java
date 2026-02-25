package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.prodottomagazzino.ProdottoMagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.prodottomagazzino.ProdottoMagazzinoResponse;
import it.spindox.stagelab.magazzino.entities.ProdottoMagazzino;
import it.spindox.stagelab.magazzino.entities.ScortaMinPMStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;



@Slf4j
@Component
public class ProdottoMagazzinoMapperImpl implements ProdottoMagazzinoMapper {


    // DTO : ENTITY (CREATE)

    @Override
    public ProdottoMagazzino toEntity(ProdottoMagazzinoRequest request) {

        if (request == null) {
            log.warn("toEntity chiamato con request NULL");
            return null;
        }

        ProdottoMagazzino pm = new ProdottoMagazzino();

        // QUANTITÀ (campo numerico)

        pm.setQuantita(request.getQuantita());


        log.debug("[PM-MAPPER] toEntity -> quantita={}", request.getQuantita());

        return pm;
    }



    // PATCH UPDATE : ENTITY ← REQUEST

    @Override
    public void updateEntity(ProdottoMagazzino entity, ProdottoMagazzinoRequest request) {
        if (entity == null || request == null) {
            log.warn("updateEntity chiamato con entity={} request={}", entity, request);
            return;
        }

        if (request.getQuantita() != null) {
            entity.setQuantita(request.getQuantita());
            log.debug("[PM-MAPPER] update quantita -> {}", request.getQuantita());
        }

    }



    // ENTITY : DTO RESPONSE

    @Override
    public ProdottoMagazzinoResponse toResponse(ProdottoMagazzino entity) {

        if (entity == null) return null;

        // CALCOLO DELLO STATO SCORTA (runtime, enum)

        ScortaMinPMStatus status = ScortaMinPMStatus.fromQuantita(entity.getQuantita());

        return ProdottoMagazzinoResponse.builder()
                .id(entity.getId())
                .prodottoId(entity.getProdotto().getId())
                .nomeProdotto(entity.getProdotto().getNome())
                .magazzinoId(entity.getMagazzino().getId())
                .nomeMagazzino(entity.getMagazzino().getNome())
                .quantita(entity.getQuantita())

                // VALORI STATO (calcolato)
                .scortaMinStatus(status)

                // STATO SALVATO SUL DB (String)
                .scortaMinStatusDb(entity.getScortaMinStatus())

                .build();
    }
}