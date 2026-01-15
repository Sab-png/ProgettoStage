package it.spindox.stagelab.magazzino.repositories;

import it.spindox.stagelab.magazzino.entities.Fattura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FatturaRepository extends JpaRepository<Fattura, Long> {

    List<Fattura> findByProdottoId(Long prodottoId);

    @Query("""
        SELECT f FROM Fattura f
        WHERE (:dataDa IS NULL OR f.dataFattura >= :dataDa)
        AND (:dataA IS NULL OR f.dataFattura <= :dataA)
        AND (:importoMin IS NULL OR f.importo >= :importoMin)
        AND (:importoMax IS NULL OR f.importo <= :importoMax)
    """)
    Page<Fattura> search(
            @Param("dataDa") java.time.LocalDate dataDa,
            @Param("dataA") java.time.LocalDate dataA,
            @Param("importoMin") Double importoMin,
            @Param("importoMax") Double importoMax,
            Pageable pageable
    );
}
