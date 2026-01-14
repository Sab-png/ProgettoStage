package it.spindox.stagelab.magazzino.repositories;

import it.spindox.stagelab.magazzino.entities.ProdottoMagazzino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdottoMagazzinoRepository extends JpaRepository<ProdottoMagazzino, Long> {
}
