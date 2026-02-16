package it.spindox.stagelab.magazzino.repositories;
import it.spindox.stagelab.magazzino.entities.JobExecution;
import it.spindox.stagelab.magazzino.entities.StatusJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;



@Repository
public interface JobExecutionRepository extends JpaRepository<JobExecution, Long> {

    // SEARCH COMPLETA

    @Query("""
        SELECT j
        FROM JobExecution j
        WHERE (:status IS NULL OR j.status = :status)
          AND (:startFrom IS NULL OR j.startTime >= :startFrom)
          AND (:startTo IS NULL OR j.startTime <= :startTo)
          AND (:hasError IS NULL OR
               (:hasError = TRUE AND j.errorMessage IS NOT NULL) OR
               (:hasError = FALSE AND j.errorMessage IS NULL))
    """)
    Page<JobExecution> search(
            @Param("status") StatusJob status,
            @Param("startFrom") LocalDateTime startFrom,
            @Param("startTo") LocalDateTime startTo,
            @Param("hasError") Boolean hasError,
            Pageable pageable
    );

    // GET ALL SOLO ID
    @Query("""
        SELECT j.id
        FROM JobExecution j
        WHERE (:status IS NULL OR j.status = :status)
          AND (:startFrom IS NULL OR j.startTime >= :startFrom)
          AND (:startTo IS NULL OR j.startTime <= :startTo)
          AND (:hasError IS NULL OR
               (:hasError = TRUE AND j.errorMessage IS NOT NULL) OR
               (:hasError = FALSE AND j.errorMessage IS NULL))
    """)
    Page<Long> searchIds(
            @Param("status") StatusJob status,
            @Param("startFrom") LocalDateTime startFrom,
            @Param("startTo") LocalDateTime startTo,
            @Param("hasError") Boolean hasError,
            Pageable pageable
    );

    Optional<JobExecution> findFirstByOrderByStartTimeDesc();
    Optional<JobExecution> findFirstByStatus(StatusJob status);
}