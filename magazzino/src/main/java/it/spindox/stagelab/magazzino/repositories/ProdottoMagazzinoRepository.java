package it.spindox.stagelab.magazzino.repositories;

import it.spindox.stagelab.magazzino.entities.ProdottoMagazzino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProdottoMagazzinoRepository extends JpaRepository<ProdottoMagazzino, Long> {

    @Query("""
        SELECT pm FROM ProdottoMagazzino pm
        WHERE pm.prodotto.id = :prodottoId
        AND pm.magazzino.id = :magazzinoId
    """)
    Optional<ProdottoMagazzino> findByProdottoIdAndMagazzinoId(
            @Param("prodottoId") Long prodottoId,
            @Param("magazzinoId") Long magazzinoId
    );
}
