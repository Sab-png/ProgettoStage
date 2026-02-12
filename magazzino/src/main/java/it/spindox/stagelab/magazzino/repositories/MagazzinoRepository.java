
package it.spindox.stagelab.magazzino.repositories;
import it.spindox.stagelab.magazzino.entities.Magazzino;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MagazzinoRepository extends JpaRepository<Magazzino, Long> {

    // SEARCH COMPLETA
    @Query("""
        SELECT DISTINCT m
        FROM Magazzino m
        WHERE (:id IS NULL OR m.id = :id)
          AND (:nome IS NULL OR LOWER(m.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
          AND (:indirizzo IS NULL OR LOWER(m.indirizzo) LIKE LOWER(CONCAT('%', :indirizzo, '%')))
          AND (:capacitaMin IS NULL OR m.capacita >= :capacitaMin)
          AND (:capacitaMax IS NULL OR m.capacita <= :capacitaMax)
    """)
    Page<Magazzino> search(
            @Param("id") Long id,
            @Param("nome") String nome,
            @Param("indirizzo") String indirizzo,
            @Param("capacitaMin") Integer capacitaMin,
            @Param("capacitaMax") Integer capacitaMax,
            Pageable pageable
    );

    // GET ALL SOLO ID
    @Query("""
        SELECT m.id
        FROM Magazzino m
        WHERE (:nome IS NULL OR LOWER(m.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
          AND (:indirizzo IS NULL OR LOWER(m.indirizzo) LIKE LOWER(CONCAT('%', :indirizzo, '%')))
          AND (:capacitaMin IS NULL OR m.capacita >= :capacitaMin)
          AND (:capacitaMax IS NULL OR m.capacita <= :capacitaMax)
    """)
    Page<Long> searchIds(
            @Param("nome") String nome,
            @Param("indirizzo") String indirizzo,
            @Param("capacitaMin") Integer capacitaMin,
            @Param("capacitaMax") Integer capacitaMax,
            Pageable pageable
    );
}