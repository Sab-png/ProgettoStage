package it.spindox.stagelab.magazzino.services;

import it.spindox.stagelab.magazzino.dto.request.ProdottoRequest;
import it.spindox.stagelab.magazzino.dto.request.ProdottoSearchRequest;
import it.spindox.stagelab.magazzino.dto.response.ProdottoResponse;
import it.spindox.stagelab.magazzino.entities.Prodotto;
import it.spindox.stagelab.magazzino.repositories.ProdottoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

public interface ProdottoService {

    ProdottoResponse getProdotto(Long id);

    ProdottoResponse saveProdotto(ProdottoRequest request);

    ProdottoResponse editProdotto(Long id, ProdottoRequest request);

    Page<ProdottoResponse> searchProdotto(ProdottoSearchRequest request);
}
