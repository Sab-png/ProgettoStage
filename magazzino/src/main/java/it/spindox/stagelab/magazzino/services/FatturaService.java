package it.spindox.stagelab.magazzino.services;

import it.spindox.stagelab.magazzino.dto.request.FatturaRequest;
import it.spindox.stagelab.magazzino.dto.request.FatturaSearchRequest;
import it.spindox.stagelab.magazzino.dto.response.FatturaResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FatturaService {

    FatturaResponse getFattura(Long id);

    List<FatturaResponse> getFattureByProdotto(Long prodottoId);

    FatturaResponse saveFattura(FatturaRequest request);

    FatturaResponse editFattura(Long id, FatturaRequest request);

    Page<FatturaResponse> searchFattura(FatturaSearchRequest request);
}
