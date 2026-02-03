package it.spindox.stagelab.magazzino.repositories;
import it.spindox.stagelab.magazzino.entities.ProdottoMagazzino;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProdottoMagazzinoRepository extends JpaRepository<ProdottoMagazzino, Long> {

    @Query("""
        SELECT DISTINCT pm
        FROM ProdottoMagazzino pm
        JOIN pm.prodotto p
        JOIN pm.magazzino m
        WHERE (:id IS NULL OR pm.id = :id)
          AND (:prodottoId IS NULL OR p.id = :prodottoId)
          AND (:magazzinoId IS NULL OR m.id = :magazzinoId)
          AND (:quantitaMin IS NULL OR pm.quantita >= :quantitaMin)
          AND (:quantitaMax IS NULL OR pm.quantita <= :quantitaMax)
          AND (:nomeProdotto IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :nomeProdotto, '%')))
          AND (:nomeMagazzino IS NULL OR LOWER(m.nome) LIKE LOWER(CONCAT('%', :nomeMagazzino, '%')))
    """)
    Page<ProdottoMagazzino> search(
            @Param("id") Long id,
            @Param("prodottoId") Long prodottoId,
            @Param("magazzinoId") Long magazzinoId,
            @Param("quantitaMin") Integer quantitaMin,
            @Param("quantitaMax") Integer quantitaMax,
            @Param("nomeProdotto") String nomeProdotto,
            @Param("nomeMagazzino") String nomeMagazzino,
            Pageable pageable
    );
}
