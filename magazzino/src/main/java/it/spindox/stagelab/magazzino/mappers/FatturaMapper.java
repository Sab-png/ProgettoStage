
package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;
import it.spindox.stagelab.magazzino.entities.Fattura;
import it.spindox.stagelab.magazzino.entities.Prodotto;

public interface FatturaMapper {

    Fattura toEntity(FatturaRequest request, Prodotto prodotto);

    FatturaResponse toResponse(Fattura entity);

    void updateEntity(Fattura target, FatturaRequest request, Prodotto prodotto);
}
