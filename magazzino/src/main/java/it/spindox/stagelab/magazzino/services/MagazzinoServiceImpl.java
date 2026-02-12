package it.spindox.stagelab.magazzino.services;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoResponse;
import it.spindox.stagelab.magazzino.entities.Magazzino;
import it.spindox.stagelab.magazzino.entities.ProdottoMagazzino;
import it.spindox.stagelab.magazzino.entities.StockStatusMagazzino;
import it.spindox.stagelab.magazzino.mappers.MagazzinoMapper;
import it.spindox.stagelab.magazzino.repositories.MagazzinoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;




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

    @Override
    public void create(MagazzinoRequest request) {

    }

    @Override
    public void update(Long id, MagazzinoRequest request) {

    }

    @Override
    public Page<MagazzinoResponse> search(MagazzinoRequest request) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    @Transactional
    public void checkStockLevels() {

        List<Magazzino> magazzini = repository.findAll();

        for (Magazzino m : magazzini) {

            int totale = 0;
            if (m.getProdottiMagazzino() != null) {
                totale = m.getProdottiMagazzino().stream()
                        .map(ProdottoMagazzino::getQuantita)
                        .filter(Objects::nonNull)
                        .mapToInt(Integer::intValue)
                        .sum();
            }

            int capacita = (m.getCapacita() != null && m.getCapacita() > 0)
                    ? m.getCapacita()
                    : 1;

            double percentuale = (totale * 100.0) / capacita;

            StockStatusMagazzino stato = StockStatusMagazzino.fromPercentuale(percentuale);

            m.setStockStatus(stato);
            repository.save(m);

            log.info("Magazzino {} → Totale: {} | {}% → Stato: {}",
                    m.getNome(), totale, String.format("%.2f", percentuale), stato);
        }
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
