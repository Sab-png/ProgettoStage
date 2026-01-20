
package it.spindox.stagelab.magazzino.controllers;
import it.spindox.stagelab.magazzino.dto.magazzino.*;
import it.spindox.stagelab.magazzino.entities.Magazzino;
import it.spindox.stagelab.magazzino.services.MagazzinoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST per la gestione del Magazzino.
 */
@RestController
@RequestMapping("/magazzino")
@RequiredArgsConstructor
public class MagazzinoController {

    private final MagazzinoService magazzinoService;

    /**
     * READ - Recupera un elemento di magazzino dato il suo ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MagazzinoResponse> getMagazzino(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(magazzinoService.getById(id));
    }

    /**
     * CREATE - Salva un nuovo elemento di magazzino.
     */
    @PostMapping
    public ResponseEntity<Void> saveMagazzino(
            @Valid @RequestBody MagazzinoRequest request
    ) {
        magazzinoService.create(request);
        return ResponseEntity.status(201).build(); // CREATED
    }

    /**
     * UPDATE - Aggiornamento parziale (PATCH).
     * Vengono aggiornati solo i campi non null.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateMagazzino(
            @PathVariable Long id,
            @Valid @RequestBody Magazzino request
    ) {
        magazzinoService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE - Elimina un elemento di magazzino.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMagazzino(
            @PathVariable Long id
    ) {
        magazzinoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * READ (SEARCH) - Ricerca paginata con filtri.
     */
    @PostMapping("/search")
    public ResponseEntity<Page<MagazzinoResponse>> searchMagazzino(
            @Valid @RequestBody MagazzinoRequest searchRequest
    ) {
        return ResponseEntity.ok(magazzinoService.search(searchRequest));
    }
}
