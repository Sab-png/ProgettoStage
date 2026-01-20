
package it.spindox.stagelab.magazzino.repositories;

import it.spindox.stagelab.magazzino.entities.Magazzino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository Spring Data JPA per la gestione dell'entità Magazzino.

 */
@Repository
public interface MagazzinoRepository extends JpaRepository<Magazzino, Long>{
}




