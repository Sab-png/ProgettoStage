package it.spindox.stagelab.magazzino.repositories;

import it.spindox.stagelab.magazzino.entities.Magazzino;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MagazzinoRepository extends JpaRepository<Magazzino, Long> {

    @Query("""
        SELECT m FROM Magazzino m
        JOIN m.prodottoMagazzino pm
        WHERE pm.prodotto.id = :prodottoId
    """)
    Magazzino findByProdottoId(@Param("prodottoId") Long prodottoId);

    @Query("""
        SELECT m FROM Magazzino m
        WHERE (:nome IS NULL OR m.nome LIKE %:nome%)
        AND (:indirizzo IS NULL OR m.indirizzo LIKE %:indirizzo%)
    """)
    Page<Magazzino> search(
            @Param("nome") String nome,
            @Param("indirizzo") String indirizzo,
            Pageable pageable
    );
}
