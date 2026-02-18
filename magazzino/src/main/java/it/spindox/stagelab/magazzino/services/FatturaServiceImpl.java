
package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaSearchRequest;
import it.spindox.stagelab.magazzino.entities.Fattura;
import it.spindox.stagelab.magazzino.entities.Prodotto;
import it.spindox.stagelab.magazzino.entities.SXFatturaStatus;
import it.spindox.stagelab.magazzino.exceptions.ResourceNotFoundException;
import it.spindox.stagelab.magazzino.exceptions.fatturaexceptions.FatturaAlreadyPaidException;
import it.spindox.stagelab.magazzino.exceptions.fatturaexceptions.InvalidDataFatturaException;
import it.spindox.stagelab.magazzino.exceptions.fatturaexceptions.InvalidImportoFatturaException;
import it.spindox.stagelab.magazzino.mappers.FatturaMapper;
import it.spindox.stagelab.magazzino.repositories.FatturaRepository;
import it.spindox.stagelab.magazzino.repositories.ProdottoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;




@Slf4j
@Service
@RequiredArgsConstructor
public class FatturaServiceImpl implements FatturaService {

    private final FatturaRepository fatturaRepository;
    private final ProdottoRepository prodottoRepository;
    private final FatturaMapper fatturaMapper;


    @Override
    public Page<FatturaResponse> search(FatturaSearchRequest request) {
        return null;
    }


    //  CREATE

    @Override
    public FatturaResponse create(FatturaRequest request) {

        if (request.getIdProdotto() == null) {
            throw new InvalidImportoFatturaException(null, request.getImporto(), request.getQuantita());
        }

        // Validazione date
        if (request.getDataScadenza() != null &&
                request.getDataFattura() != null &&
                request.getDataScadenza().isBefore(request.getDataFattura())) {

            throw new InvalidDataFatturaException(
                    null,
                    request.getDataFattura(),
                    request.getDataScadenza()
            );
        }

        Prodotto prodotto = prodottoRepository.findById(request.getIdProdotto())
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato"));

        Fattura entity = fatturaMapper.toEntity(request, prodotto);

        Long nextVal = fatturaRepository.nextNumeroSeq();
        entity.setNumero("FAT-" + nextVal);

        entity = fatturaRepository.save(entity);

        return fatturaMapper.toResponse(entity);
    }



    //  UPDATE (PATCH)

    @Override
    public FatturaResponse update(Long id, FatturaRequest request) {

        Fattura entity = fatturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fattura non trovata"));

        // Non si puo'  modificare una fattura già pagata

        if (entity.getStatus() == SXFatturaStatus.PAGATA) {
            throw new FatturaAlreadyPaidException(id);
        }

        // Validazione date
        if (request.getDataFattura() != null &&
                request.getDataScadenza() != null &&
                request.getDataScadenza().isBefore(request.getDataFattura())) {

            throw new InvalidDataFatturaException(
                    id,
                    request.getDataFattura(),
                    request.getDataScadenza()
            );
        }

        Prodotto prodotto = null;

        if (request.getIdProdotto() != null) {
            prodotto = prodottoRepository.findById(request.getIdProdotto())
                    .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato"));
        }

        fatturaMapper.updateEntity(entity, request, prodotto);

        // Salva la fattura e poi ricalcola lo stato nel mapper

        entity = fatturaRepository.save(entity);

        return fatturaMapper.toResponse(entity);
    }



    // GET BY ID

    @Override
    public FatturaResponse getById(Long id) {
        Fattura entity = fatturaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fattura non trovata"));
        return fatturaMapper.toResponse(entity);
    }

    @Override
    public PageImpl<FatturaResponse> getByProdotto(Long idProdotto, int page, int size) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Page<Long> searchIds(FatturaSearchRequest req) {
        return null;
    }

    @Override
    public Page<FatturaResponse> getAllPaged(int page, int size) {
        return null;
    }
}