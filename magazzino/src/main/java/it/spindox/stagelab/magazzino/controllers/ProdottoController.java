package it.spindox.stagelab.magazzino.controllers;

import it.spindox.stagelab.magazzino.entities.Prodotto;
import it.spindox.stagelab.magazzino.repositories.ProdottoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prodotti")
public class ProdottoController {

    private final ProdottoRepository prodottoRepository;

    public ProdottoController(ProdottoRepository prodottoRepository) {
        this.prodottoRepository = prodottoRepository;
    }

    @GetMapping
    public ResponseEntity<List<Prodotto>> getAllProdotti() {
        return ResponseEntity.ok(prodottoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prodotto> getProdottoById(@PathVariable Long id) {
        return prodottoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Prodotto> createProdotto(@RequestBody Prodotto prodotto) {
        Prodotto savedProdotto = prodottoRepository.save(prodotto);
        return ResponseEntity.ok(savedProdotto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Prodotto> updateProdotto(
            @PathVariable Long id,
            @RequestBody Prodotto prodotto) {

        return prodottoRepository.findById(id)
                .map(existingProdotto -> {
                    existingProdotto.setNome(prodotto.getNome());
                    existingProdotto.setDescrizione(prodotto.getDescrizione());
                    existingProdotto.setPrezzo(prodotto.getPrezzo());
                    existingProdotto.setFattura(prodotto.getFattura());
                    existingProdotto.setProdottoMagazzino(prodotto.getProdottoMagazzino());
                    return ResponseEntity.ok(prodottoRepository.save(existingProdotto));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProdotto(@PathVariable Long id) {
        if (!prodottoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        prodottoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
