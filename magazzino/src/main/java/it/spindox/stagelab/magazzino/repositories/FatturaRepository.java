
package it.spindox.stagelab.magazzino.repositories;
import it.spindox.stagelab.magazzino.entities.Fattura;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FatturaRepository extends JpaRepository<Fattura, Long> {

    /**
     * Search paginata con filtri opzionali:
     * - idProdotto
     * - dataFatturaFrom/To   (intervallo date)
     * - importoMin/Max       (range importo)
     *
     */
    @Query("""
        SELECT f
        FROM Fattura f
        WHERE (:idProdotto IS NULL OR f.prodotto.id = :idProdotto)
          AND (:dataFrom IS NULL OR f.dataFattura >= :dataFrom)
          AND (:dataTo   IS NULL OR f.dataFattura <= :dataTo)
          AND (:importoMin IS NULL OR f.importo >= :importoMin)
          AND (:importoMax IS NULL OR f.importo <= :importoMax)
    """)
    Page<Fattura> search(
            @Param("idProdotto") Long idProdotto,
            @Param("dataFrom") LocalDate dataFrom,
            @Param("dataTo") LocalDate dataTo,
            @Param("importoMin") Double importoMin,
            @Param("importoMax") Double importoMax,
            Pageable pageable
    );
}