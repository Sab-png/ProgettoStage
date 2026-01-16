
package it.spindox.stagelab.magazzino.controllers;
import it.spindox.stagelab.magazzino.dto.fattura.*;
import it.spindox.stagelab.magazzino.services.FatturaService;
import jakarta.validation.Valid; // Controlla automaticamente i dati in ingresso
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// Spring usa queste annotation per collegare
// una richiesta HTTP ai parametri dei metodi del controller
// es: @PathVariable, @RequestBody

/**
 * Controller REST per la gestione delle Fatture.
 */
@RestController
@RequestMapping("/fatture")
@RequiredArgsConstructor
public class FatturaController {

    private final FatturaService fatturaService;

    /**
     * Recupera una fattura dato il suo ID.
     * Restituisce 404 se la fattura non esiste.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FatturaResponse> getFattura(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok((FatturaResponse) fatturaService.getById(id));
    }

    /**
     * Recupera le fatture associate a un prodotto
     *
     */
    @GetMapping("/prodotto/{idProdotto}")
    public ResponseEntity getFattureByProdotto(
            @PathVariable Long idProdotto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                fatturaService.getByProdotto(idProdotto, page, size)
        );
    }

    /**
     * Salva una nuova fattura associata a un prodotto.
     */
    @PostMapping
    public ResponseEntity<Void> saveFattura(
            @Valid @RequestBody FatturaCreateRequest request
    ) {
        fatturaService.create(request);
        return ResponseEntity.ok().build();
    }

    /**
     * Modifica una fattura esistente dato il suo ID.
     * con il comando PATCH: vengono aggiornati solo i campi non null.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Void> editFattura(
            @PathVariable Long id,
            @Valid @RequestBody FatturaUpdateRequest request
    ) {
        fatturaService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    /**
     * Ricerca paginata di fatture applicando filtri.
     */
    @PostMapping("/search")
    public ResponseEntity searchFattura(
            @Valid @RequestBody FatturaSearchRequest searchRequest
    ) {
        return ResponseEntity.ok(fatturaService.search(searchRequest));
    }
}
