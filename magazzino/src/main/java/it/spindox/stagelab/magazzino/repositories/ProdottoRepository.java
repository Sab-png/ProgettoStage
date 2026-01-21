
package it.spindox.stagelab.magazzino.repositories;

import it.spindox.stagelab.magazzino.entities.Prodotto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {

    /**
     * Search  con filtri opzionali:
     * - nome
     * - descrizione
     * - prezzoMin / prezzoMax (range)
     */
    @Query("""
        SELECT p
        FROM Prodotto p
        WHERE (:nome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
          AND (:descrizione IS NULL OR LOWER(p.descrizione) LIKE LOWER(CONCAT('%', :descrizione, '%')))
          AND (:prezzoMin IS NULL OR p.prezzo >= :prezzoMin)
          AND (:prezzoMax IS NULL OR p.prezzo <= :prezzoMax)
    """)
    Page<Prodotto> search(
            @Param("nome") String nome,
            @Param("descrizione") String descrizione,
            @Param("prezzoMin") Double prezzoMin,
            @Param("prezzoMax") Double prezzoMax,
            Pageable pageable
    );

Page<Prodotto> search(String s, String s1, String s2, Pageable pageable);}
