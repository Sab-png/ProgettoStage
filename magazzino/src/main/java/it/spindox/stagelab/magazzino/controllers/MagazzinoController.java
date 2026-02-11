
package it.spindox.stagelab.magazzino.controllers;

import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoResponse;
import it.spindox.stagelab.magazzino.services.MagazzinoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/magazzino")
@RequiredArgsConstructor
public class MagazzinoController {

    private final MagazzinoService magazzinoService;

    @GetMapping("/{id}")
    public ResponseEntity<MagazzinoResponse> getMagazzino(@PathVariable Long id) {
        return ResponseEntity.ok(magazzinoService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Void> saveMagazzino(@Valid @RequestBody MagazzinoRequest request) {
        magazzinoService.create(request);
        return ResponseEntity.status(201).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateMagazzino(
            @PathVariable Long id,
            @Valid @RequestBody MagazzinoRequest request
    ) {
        magazzinoService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMagazzino(@PathVariable Long id) {
        magazzinoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/search")
    public ResponseEntity<Page<MagazzinoResponse>> searchMagazzino(
            @Valid @RequestBody MagazzinoRequest searchRequest
    ) {
        return ResponseEntity.ok(magazzinoService.search(searchRequest));
    }

    // METODO -CHECK STOCK

    @PostMapping("/check-stock")
    public ResponseEntity<Void> checkStockLevels() {
        magazzinoService.checkStockLevels();
        return ResponseEntity.ok().build();
    }
}
