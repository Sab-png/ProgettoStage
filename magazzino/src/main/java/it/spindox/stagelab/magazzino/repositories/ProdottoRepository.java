package it.spindox.stagelab.magazzino.repositories;

import it.spindox.stagelab.magazzino.entities.Prodotto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {

    @Query("""
        SELECT p FROM Prodotto p
        WHERE (:nome IS NULL OR p.nome LIKE %:nome%)
        AND (:prezzoMin IS NULL OR p.prezzo >= :prezzoMin)
        AND (:prezzoMax IS NULL OR p.prezzo <= :prezzoMax)
    """)
    Page<Prodotto> search(
            @Param("nome") String nome,
            @Param("prezzoMin") Double prezzoMin,
            @Param("prezzoMax") Double prezzoMax,
            Pageable pageable
    );
}
