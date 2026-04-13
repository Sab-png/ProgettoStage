package it.spindox.stagelab.magazzino.mappers;

import it.spindox.stagelab.magazzino.dto.response.FatturaResponse;
import it.spindox.stagelab.magazzino.entities.Fattura;

public class FatturaMapper {

    public static FatturaResponse toResponse(Fattura fattura) {
        FatturaResponse response = new FatturaResponse();
        response.setId(fattura.getId());
        response.setDataFattura(fattura.getDataFattura());
        response.setImporto(fattura.getImporto());
        response.setProdottoId(fattura.getProdotto().getId());
        return response;
    }
}
