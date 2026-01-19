package it.spindox.stagelab.magazzino.repositories;

import it.spindox.stagelab.magazzino.entities.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository JPA per la gestione dei prodotti.
 * <p>
 * Estende JpaRepository e fornisce:
 * - CRUD completo
 * - Query automatiche
 * - Supporto alla paginazione
 */
@Repository
public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {
}
