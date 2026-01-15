package it.spindox.stagelab.magazzino.mappers;

import it.spindox.stagelab.magazzino.dto.response.MagazzinoResponse;
import it.spindox.stagelab.magazzino.entities.Magazzino;

public class MagazzinoMapper {

    public static MagazzinoResponse toResponse(Magazzino magazzino) {
        MagazzinoResponse response = new MagazzinoResponse();
        response.setId(magazzino.getId());
        response.setNome(magazzino.getNome());
        response.setIndirizzo(magazzino.getIndirizzo());
        response.setCapacita(magazzino.getCapacita());
        return response;
    }
}
