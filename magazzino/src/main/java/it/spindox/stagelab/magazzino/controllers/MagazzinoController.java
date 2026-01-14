package it.spindox.stagelab.magazzino.controllers;

import it.spindox.stagelab.magazzino.entities.Magazzino;
import it.spindox.stagelab.magazzino.repositories.MagazzinoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/magazzini")
public class MagazzinoController {

    private final MagazzinoRepository magazzinoRepository;

    public MagazzinoController(MagazzinoRepository magazzinoRepository) {
        this.magazzinoRepository = magazzinoRepository;
    }

    @GetMapping
    public ResponseEntity<List<Magazzino>> getAllMagazzini() {
        return ResponseEntity.ok(magazzinoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Magazzino> getMagazzinoById(@PathVariable Long id) {
        return magazzinoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Magazzino> createMagazzino(@RequestBody Magazzino magazzino) {
        Magazzino savedMagazzino = magazzinoRepository.save(magazzino);
        return ResponseEntity.ok(savedMagazzino);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Magazzino> updateMagazzino(
            @PathVariable Long id,
            @RequestBody Magazzino magazzino) {

        return magazzinoRepository.findById(id)
                .map(existingMagazzino -> {
                    existingMagazzino.setNome(magazzino.getNome());
                    existingMagazzino.setIndirizzo(magazzino.getIndirizzo());
                    existingMagazzino.setCapacita(magazzino.getCapacita());
                    existingMagazzino.setProdottoMagazzino(magazzino.getProdottoMagazzino());
                    return ResponseEntity.ok(magazzinoRepository.save(existingMagazzino));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMagazzino(@PathVariable Long id) {
        if (!magazzinoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        magazzinoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
