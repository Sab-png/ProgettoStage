package it.spindox.stagelab.magazzino.repositories;
import it.spindox.stagelab.magazzino.entities.FatturaWorkExecution;
import it.spindox.stagelab.magazzino.entities.SXFatturaJobexecution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.OffsetDateTime;
import java.util.Optional;




@Repository
public interface FatturaWorkExecutionRepository extends JpaRepository<FatturaWorkExecution, Long> {


    // Ricerca base status, time, error

    @Query("""
        SELECT f
        FROM FatturaWorkExecution f
        WHERE (:status IS NULL OR f.status = :status)
          AND (:startFrom IS NULL OR f.startTime >= :startFrom)
          AND (:startTo IS NULL OR f.startTime <= :startTo)
          AND (
                :hasError IS NULL OR
                (:hasError = TRUE AND f.errorMessage IS NOT NULL) OR
                (:hasError = FALSE AND f.errorMessage IS NULL)
              )
    """)
    Page<FatturaWorkExecution> search(
            @Param("status") SXFatturaJobexecution status,
            @Param("startFrom") OffsetDateTime startFrom,
            @Param("startTo") OffsetDateTime startTo,
            @Param("hasError") Boolean hasError,
            Pageable pageable
    );


    // Ricerca base SOLO ID

    @Query("""
        SELECT f.id
        FROM FatturaWorkExecution f
        WHERE (:status IS NULL OR f.status = :status)
          AND (:startFrom IS NULL OR f.startTime >= :startFrom)
          AND (:startTo IS NULL OR f.startTime <= :startTo)
          AND (
                :hasError IS NULL OR
                (:hasError = TRUE AND f.errorMessage IS NOT NULL) OR
                (:hasError = FALSE AND f.errorMessage IS NULL)
              )
    """)
    Page<Long> searchIds(
            @Param("status") SXFatturaJobexecution status,
            @Param("startFrom") OffsetDateTime startFrom,
            @Param("startTo") OffsetDateTime startTo,
            @Param("hasError") Boolean hasError,
            Pageable pageable
    );


    // Ricerca avanzata con fatturaId

    @Query("""
        SELECT f
        FROM FatturaWorkExecution f
        WHERE (:status IS NULL OR f.status = :status)
          AND (:startFrom IS NULL OR f.startTime >= :startFrom)
          AND (:startTo IS NULL OR f.startTime <= :startTo)
          AND (:fatturaId IS NULL OR f.fatturaId = :fatturaId)
          AND (
                :hasError IS NULL OR
                (:hasError = TRUE AND f.errorMessage IS NOT NULL) OR
                (:hasError = FALSE AND f.errorMessage IS NULL)
              )
    """)
    Page<FatturaWorkExecution> searchWorkExecutions(
            @Param("status") SXFatturaJobexecution status,
            @Param("startFrom") OffsetDateTime startFrom,
            @Param("startTo") OffsetDateTime startTo,
            @Param("fatturaId") Long fatturaId,
            @Param("hasError") Boolean hasError,
            Pageable pageable
    );

}