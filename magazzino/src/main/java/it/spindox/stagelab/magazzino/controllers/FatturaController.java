
package it.spindox.stagelab.magazzino.controllers;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;
import it.spindox.stagelab.magazzino.entities.Fattura;
import it.spindox.stagelab.magazzino.services.FatturaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Range;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

/**
 * Controller REST per la gestione delle Fatture.
 */
@RestController
@RequestMapping("/fatture")
@RequiredArgsConstructor
@Validated
public class FatturaController {

    private final FatturaService fatturaService;

    /**
     * Recupera una fattura dato il suo ID.
     * Restituisce 404 se la fattura non esiste
     */
    @GetMapping("/{id}")
    public ResponseEntity<FatturaResponse> getFattura(@PathVariable Long id) {
        return ResponseEntity.ok(fatturaService.getById(id));
    }

    /**
     * Recupera le fatture associate a un prodotto
     */
    @GetMapping("/prodotto/{idProdotto}")
    public ResponseEntity<Range> getFattureByProdotto(
            @PathVariable Long idProdotto,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size
    ) {
        Range result;
        result = fatturaService.getByProdotto(idProdotto, page, size);
        return ResponseEntity.ok(result);
    }

    /**
     * Crea una nuova fattura.
     * Ritorna 201 Created con Location e body -DTO creato.
     */
    @PostMapping
    public ResponseEntity<Fattura> saveFattura(
            @Valid @RequestBody FatturaRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        Fattura created = fatturaService.create(request);
        URI location = uriBuilder.path("/fatture/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    /**
     * Aggiorna parzialmente una fattura (PATCH = campi non null).
     * Ritorna il DTO aggiornato
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Fattura> editFattura(
            @PathVariable Long id,
            @Valid @RequestBody FatturaRequest request
    ) {
        Fattura updated = fatturaService.update(id, request);
        return ResponseEntity.ok(updated);
        // Se preferisci 204 senza body: return ResponseEntity.noContent().build();
    }

    /**
     * Ricerca paginata di fatture applicando filtri.
     */
    @PostMapping("/search")
    public ResponseEntity<Page<Fattura>> searchFattura(
            @Valid @RequestBody FatturaRequest searchRequest
    ) {
        Page<Fattura> page = fatturaService.search(searchRequest);
        return ResponseEntity.ok(page);
    }

    /**
     * Cancella una fattura.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFattura(@PathVariable Long id) {
        fatturaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
