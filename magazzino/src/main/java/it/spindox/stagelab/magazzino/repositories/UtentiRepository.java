package it.spindox.stagelab.magazzino.repositories;
import it.spindox.stagelab.magazzino.entities.Utenti;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface UtentiRepository extends JpaRepository<Utenti, Long> {

    // LOGIN E CREAZIONE UTENTE

    Utenti findByUsername(String username);

    Utenti findByUsernameAndPassword(String username, String password);

    boolean existsByUsername(String username);

    // SEARCH COMPLETA

    @Query("""
        SELECT u
        FROM Utenti u
        WHERE (:username IS NULL 
               OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%')))
    """)
    Page<Utenti> search(
            @Param("username") String username,
            Pageable pageable
    );

    // SEARCH SOLO ID

    @Query("""
        SELECT u.id
        FROM Utenti u
        WHERE (:username IS NULL 
               OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%')))
    """)
    Page<Long> searchIds(
            @Param("username") String username,
            Pageable pageable
    );
}