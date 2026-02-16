package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoResponse;
import it.spindox.stagelab.magazzino.entities.*;
import it.spindox.stagelab.magazzino.exceptions.magazzinoexceptions.InvalidCapacityException;
import it.spindox.stagelab.magazzino.exceptions.magazzinoexceptions.MagazzinoException;
import it.spindox.stagelab.magazzino.exceptions.magazzinoexceptions.ProductQuantityException;
import it.spindox.stagelab.magazzino.exceptions.magazzinoexceptions.StockCalculationException;
import it.spindox.stagelab.magazzino.mappers.MagazzinoMapper;
import it.spindox.stagelab.magazzino.repositories.MagazzinoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;




@Slf4j
@Service
@RequiredArgsConstructor
public class MagazzinoServiceImpl implements MagazzinoService {

    private final MagazzinoRepository repository;
    private final MagazzinoMapper mapper;

    @Override
    public MagazzinoResponse getById(Long id) {
        return null;
    }

    // CREATE

    @Override
    public void create(MagazzinoRequest request) {

    }

// UPDATE

    @Override
    public void update(Long id, MagazzinoRequest request) {

    }

// SEARCH

    @Override
    public Page<MagazzinoResponse> search(MagazzinoRequest request) {
        return null;
    }

    // DELETE

    @Override
    public void delete(Long id) {

    }

    // METODO PRINCIPAL: CHECK STOCK LEVEL

    @Override
    @Transactional
    public void checkStockLevels() {

        List<Magazzino> magazzini = repository.findAll();

        for (Magazzino m : magazzini) {


            // 1) VALIDAZIONE CAPACITÀ

            Integer cap = m.getCapacita();
            if (cap == null || cap <= 0) {
                throw new InvalidCapacityException(m.getNome(), cap);
            }


            // 2) VALIDAZIONE QUANTITÀ PRODOTTI

            int totaleProdotti = getTotaleProdotti(m);


            // 3) CALCOLO PERCENTUALE E STATO ( LOGICA PER DETERMINARE L' ENUMERATION)

            double percentuale;

            try {
                percentuale = (totaleProdotti * 100.0) / cap;
            } catch (Exception e) {
                throw new StockCalculationException(
                        "Errore nel calcolo della percentuale per magazzino " + m.getNome(),
                        e
                );
            }


            // 4) DETERMINAZIONE STATUS DALLE REGOLE DI BUSINESS

            StockStatusMagazzino stato = StockStatusMagazzino.fromPercentuale(percentuale);


            // 5) SALVATAGGIO STATO MAGAZZINO

            m.setStockStatus(stato);
            repository.save(m);

            log.info("[CHECK] Magazzino={} Totale={} Percentuale={} Stato={}",
                    m.getNome(),
                    totaleProdotti,
                    String.format("%.2f", percentuale),
                    stato
            );
        }
    }

    // GET TOTAL PRODUCTS

    private static int getTotaleProdotti(Magazzino m) {
        int totaleProdotti = 0;
        try {
            for (ProdottoMagazzino p : m.getProdottiMagazzino()) {

                Integer qta = p.getQuantita();

                if (qta == null || qta < 0) {
                    throw new ProductQuantityException(
                            p.getId(), m.getNome(), qta
                    );
                }

                totaleProdotti += qta;
            }
        } catch (MagazzinoException e) {

            throw e;
        } catch (Exception e) {
            throw new StockCalculationException(
                    "Errore durante il calcolo delle quantità del magazzino " + m.getNome(),
                    e
            );
        }
        return totaleProdotti;
    }

    @Override
    public Page<Long> searchIds(MagazzinoRequest req) {
        return null;
    }

    @Override
    public Page<MagazzinoResponse> getAllPaged(int page, int size) {
        return null;
    }

}