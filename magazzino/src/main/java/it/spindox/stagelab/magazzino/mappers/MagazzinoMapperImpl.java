
package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoResponse;
import it.spindox.stagelab.magazzino.entities.Magazzino;
import it.spindox.stagelab.magazzino.entities.ProdottoMagazzino;
import it.spindox.stagelab.magazzino.entities.StockStatusMagazzino;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Objects;




@Slf4j
@Component
public class MagazzinoMapperImpl implements MagazzinoMapper {

    @Override
    public Magazzino toEntity(MagazzinoRequest request) {
        if (request == null) return null;

        Magazzino m = new Magazzino();
        m.setNome(request.getNome());
        m.setIndirizzo(request.getIndirizzo());
        if (request.getCapacita() != null) m.setCapacita(request.getCapacita());
        return m;
    }

    // RESPONSE

    @Override
    public MagazzinoResponse toResponse(Magazzino entity) {
        if (entity == null) return null;

        MagazzinoResponse r = new MagazzinoResponse();
        r.setId(entity.getId());
        r.setNome(entity.getNome());
        r.setIndirizzo(entity.getIndirizzo());
        r.setCapacita(entity.getCapacita());

        int totale = 0;
        if (entity.getProdottiMagazzino() != null) {
            totale = entity.getProdottiMagazzino().stream()
                    .map(ProdottoMagazzino::getQuantita)
                    .filter(Objects::nonNull)
                    .mapToInt(Integer::intValue)
                    .sum();
        }
        r.setQuantitaTotale(totale);

        int capacita = (entity.getCapacita() != null && entity.getCapacita() > 0)
                ? entity.getCapacita()
                : 1;

        double percentuale = (totale * 100.0) / capacita;
        r.setPercentuale(percentuale);

        StockStatusMagazzino stato = entity.getStockStatus();
        r.setStockStatus(stato);
        if (stato != null) {
            r.setStatusColor(stato.getDbValue());
            r.setStatusDescription(stato.getDescription());
        }

        return r;
    }

    // UPDATE ENTITY

    @Override
    public void updateEntity(Magazzino m, @Valid MagazzinoRequest request) {
        if (m == null || request == null) return;
        if (request.getNome() != null) m.setNome(request.getNome());
        if (request.getIndirizzo() != null) m.setIndirizzo(request.getIndirizzo());
        if (request.getCapacita() != null) m.setCapacita(request.getCapacita());
    }
}