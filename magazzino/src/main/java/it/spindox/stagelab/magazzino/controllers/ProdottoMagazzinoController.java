package it.spindox.stagelab.magazzino.controllers;

import it.spindox.stagelab.magazzino.entities.ProdottoMagazzino;
import it.spindox.stagelab.magazzino.repositories.ProdottoMagazzinoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prodotto-magazzino")
public class ProdottoMagazzinoController {

    private final ProdottoMagazzinoRepository prodottoMagazzinoRepository;

    public ProdottoMagazzinoController(ProdottoMagazzinoRepository prodottoMagazzinoRepository) {
        this.prodottoMagazzinoRepository = prodottoMagazzinoRepository;
    }

    @GetMapping
    public ResponseEntity<List<ProdottoMagazzino>> getAllProdottiMagazzino() {
        return ResponseEntity.ok(prodottoMagazzinoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdottoMagazzino> getProdottoMagazzinoById(@PathVariable Long id) {
        return prodottoMagazzinoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProdottoMagazzino> createProdottoMagazzino(
            @RequestBody ProdottoMagazzino prodottoMagazzino) {

        ProdottoMagazzino saved = prodottoMagazzinoRepository.save(prodottoMagazzino);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdottoMagazzino> updateProdottoMagazzino(
            @PathVariable Long id,
            @RequestBody ProdottoMagazzino prodottoMagazzino) {

        return prodottoMagazzinoRepository.findById(id)
                .map(existing -> {
                    existing.setProdotto(prodottoMagazzino.getProdotto());
                    existing.setMagazzino(prodottoMagazzino.getMagazzino());
                    existing.setQuantita(prodottoMagazzino.getQuantita());
                    return ResponseEntity.ok(prodottoMagazzinoRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProdottoMagazzino(@PathVariable Long id) {
        if (!prodottoMagazzinoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        prodottoMagazzinoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
