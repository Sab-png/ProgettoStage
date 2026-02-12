
package it.spindox.stagelab.magazzino.repositories;
import it.spindox.stagelab.magazzino.entities.Fattura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface FatturaRepository extends JpaRepository<Fattura, Long> {

    // SEARCH COMPLETA
    @Query("""
        SELECT DISTINCT f
        FROM Fattura f
        WHERE (:numero IS NULL OR LOWER(f.numero) LIKE LOWER(CONCAT('%', :numero, '%')))
          AND (:idProdotto IS NULL OR f.prodotto.id = :idProdotto)
          AND (:dataFrom IS NULL OR f.dataFattura >= :dataFrom)
          AND (:dataTo IS NULL OR f.dataFattura <= :dataTo)
          AND (:importoMin IS NULL OR f.importo >= :importoMin)
          AND (:importoMax IS NULL OR f.importo <= :importoMax)
    """)
    Page<Fattura> search(
            @Param("numero") String numero,
            @Param("idProdotto") Long idProdotto,
            @Param("dataFrom") LocalDate dataFrom,
            @Param("dataTo") LocalDate dataTo,
            @Param("importoMin") BigDecimal importoMin,
            @Param("importoMax") BigDecimal importoMax,
            Pageable pageable
    );

    // GET ALL SOLO ID
    @Query("""
        SELECT f.id
        FROM Fattura f
        WHERE (:numero IS NULL OR LOWER(f.numero) LIKE LOWER(CONCAT('%', :numero, '%')))
          AND (:idProdotto IS NULL OR f.prodotto.id = :idProdotto)
          AND (:dataFrom IS NULL OR f.dataFattura >= :dataFrom)
          AND (:dataTo IS NULL OR f.dataFattura <= :dataTo)
          AND (:importoMin IS NULL OR f.importo >= :importoMin)
          AND (:importoMax IS NULL OR f.importo <= :importoMax)
    """)
    Page<Long> searchIds(
            @Param("numero") String numero,
            @Param("idProdotto") Long idProdotto,
            @Param("dataFrom") LocalDate dataFrom,
            @Param("dataTo") LocalDate dataTo,
            @Param("importoMin") BigDecimal importoMin,
            @Param("importoMax") BigDecimal importoMax,
            Pageable pageable
    );

    // SEQUENCE ORACLE
    @Query(value = "SELECT FATTURA_SEQ.NEXTVAL FROM DUAL", nativeQuery = true)
    Long nextNumeroSeq();
}