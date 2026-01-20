package it.spindox.stagelab.magazzino.repositories;
import it.spindox.stagelab.magazzino.entities.Fattura;
import org.springframework.data.domain.Page;import org.springframework.data.domain.Pageable;import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository JPA per la gestione delle fatture.
 */
@Repository
public interface FatturaRepository extends JpaRepository<Fattura, Long> {
Page<Fattura> findByProdottoId(Long idProdotto, Pageable pageable);}
