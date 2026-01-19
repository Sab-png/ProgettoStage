
package it.spindox.stagelab.magazzino.controllers;
import it.spindox.stagelab.magazzino.dto.magazzino.*;
import it.spindox.stagelab.magazzino.services.MagazzinoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 * Page è un’interfaccia di Spring Data
 * serve a restituire dati “a pagine” specifici,  invece che tutti insieme, includendo automaticamente informazioni sulla paginazione(anche per i metadati)
 */

/**
 * Controller REST per la gestione del Magazzino.
 */
@RestController
@RequestMapping("/magazzino")
@RequiredArgsConstructor
public class MagazzinoController {

    private final MagazzinoService magazzinoService;

    /**
     * Recupera un elemento di magazzino dato il suo ID.
     * Restituisce 404 se il magazzino non esiste.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MagazzinoResponse> getMagazzino(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(magazzinoService.getById(id));
    }

    /**
     * Salva un nuovo elemento di magazzino.
     */
    @PostMapping
    public ResponseEntity<Void> saveMagazzino(
            @Valid @RequestBody MagazzinoCreateRequest request
    ) {
        magazzinoService.create(request);
        return ResponseEntity.ok().build();
    }

    /**
     * Ricerca paginata di elementi di magazzino applicando filtri.
     */
    @PostMapping("/search")
    public ResponseEntity<Page<MagazzinoResponse>> searchMagazzino(
            @Valid @RequestBody MagazzinoSearchRequest searchRequest
    ) {
        return ResponseEntity.ok(magazzinoService.search(searchRequest));
    }
}
