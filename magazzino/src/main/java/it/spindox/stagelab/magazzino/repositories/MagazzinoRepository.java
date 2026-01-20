
package it.spindox.stagelab.magazzino.repositories;

import it.spindox.stagelab.magazzino.entities.Magazzino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository Spring Data JPA per la gestione dell'entità Magazzino.
 * <p>
 * Estendendo JpaRepository si ottengono automaticamente
 * tutte le operazioni CRUD di base:
 * - save
 * - findById
 * - findAll
 * - deleteById
 * - ecc.
 */
@Repository
public interface MagazzinoRepository extends JpaRepository<Magazzino, Long>{
}


/**
 * Recupera un elemento di Magazzino associato a un determinato prodotto.
 * Spring Data JPA genera automaticamente l'implementazione
 * del metodo a partire dal nome:
 *   findByProdottoId
 * a un prodotto &egrave; associato al massimo un record di Magazzino.
 *
 **/
