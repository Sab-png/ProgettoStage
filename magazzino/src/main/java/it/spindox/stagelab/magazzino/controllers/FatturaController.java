package it.spindox.stagelab.magazzino.controllers;

import it.spindox.stagelab.magazzino.entities.Fattura;
import it.spindox.stagelab.magazzino.repositories.FatturaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fatture")
public class FatturaController {

    private final FatturaRepository fatturaRepository;

    public FatturaController(FatturaRepository fatturaRepository) {
        this.fatturaRepository = fatturaRepository;
    }

    @GetMapping
    public ResponseEntity<List<Fattura>> getAllFatture() {
        return ResponseEntity.ok(fatturaRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fattura> getFatturaById(@PathVariable Long id) {
        return fatturaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Fattura> createFattura(@RequestBody Fattura fattura) {
        Fattura savedFattura = fatturaRepository.save(fattura);
        return ResponseEntity.ok(savedFattura);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fattura> updateFattura(
            @PathVariable Long id,
            @RequestBody Fattura fattura) {

        return fatturaRepository.findById(id)
                .map(existingFattura -> {
                    existingFattura.setDataFattura(fattura.getDataFattura());
                    existingFattura.setImporto(fattura.getImporto());
                    existingFattura.setProdotto(fattura.getProdotto());
                    return ResponseEntity.ok(fatturaRepository.save(existingFattura));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFattura(@PathVariable Long id) {
        if (!fatturaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        fatturaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
