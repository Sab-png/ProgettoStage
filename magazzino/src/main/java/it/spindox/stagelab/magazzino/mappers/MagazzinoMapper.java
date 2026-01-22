
package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoResponse;
import it.spindox.stagelab.magazzino.entities.Magazzino;import jakarta.validation.Valid;

public interface MagazzinoMapper {

    Magazzino toEntity(MagazzinoRequest request);

    MagazzinoResponse toResponse(Magazzino entity);

    void updateEntity(Magazzino magazzino, @Valid MagazzinoRequest request);
}
