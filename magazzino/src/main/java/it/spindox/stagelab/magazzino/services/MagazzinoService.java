package it.spindox.stagelab.magazzino.services;

import it.spindox.stagelab.magazzino.dto.request.MagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.request.MagazzinoSearchRequest;
import it.spindox.stagelab.magazzino.dto.response.MagazzinoResponse;
import org.springframework.data.domain.Page;

public interface MagazzinoService {

    MagazzinoResponse getMagazzino(Long id);

    MagazzinoResponse getMagazzinoByProdotto(Long prodottoId);

    MagazzinoResponse saveMagazzino(MagazzinoRequest request);

    Page<MagazzinoResponse> searchMagazzino(MagazzinoSearchRequest request);
}
