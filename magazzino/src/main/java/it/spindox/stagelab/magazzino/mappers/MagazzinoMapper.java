
package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoResponse;
import it.spindox.stagelab.magazzino.entities.Magazzino;
import jakarta.validation.Valid;

/**
 * Mapper per la conversione tra entità Magazzino e DTO.
 */
public interface MagazzinoMapper {

    /**
     * Converte un DTO in una nuova entità Magazzino.
     * Usato per CREATE.
     */
    Magazzino toEntity(MagazzinoRequest request);

    /**
     * Converte un'entità Magazzino in un DTO di risposta.
     */
    MagazzinoResponse toResponse(Magazzino entity);

    /**
     * Aggiorna un'entità Magazzino esistente usando i campi non null del DTO.
     * Usato per UPDATE.
     */
    void updateEntity(Magazzino magazzino, @Valid Magazzino request);
}
