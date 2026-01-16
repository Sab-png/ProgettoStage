
package it.spindox.stagelab.magazzino.controllers;
import it.spindox.stagelab.magazzino.dto.prodotto.*;
import it.spindox.stagelab.magazzino.services.ProdottoService;
import jakarta.validation.Valid; // Controlla automaticamente i dati in ingresso
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// Spring usa queste annotation per collegare
// una richiesta HTTP ai parametri dei metodi del controller
// es: @PathVariable, @RequestBody

/**
 * Controller REST per la gestione dei Prodotti.
 */
@RestController
@RequestMapping("/prodotti")
@RequiredArgsConstructor
public class ProdottoController {

    private final ProdottoService prodottoService;

    /**
     * Recupera un prodotto dato il suo ID.
     * Restituisce 404 se il prodotto non esiste.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProdottoResponse> getProdotto(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(prodottoService.getById(id));
    }

    /**
     * Salva un nuovo prodotto.
     */
    @PostMapping
    public ResponseEntity<Void> saveProdotto(
            @Valid @RequestBody ProdottoCreateRequest request
    ) {
        prodottoService.create(request);
        return ResponseEntity.ok().build();
    }

    /**
     * Modifica un prodotto esistente dato il suo ID.
     * PATCH: vengono aggiornati solo i campi non null.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Void> editProdotto(
            @PathVariable Long id,
            @Valid @RequestBody ProdottoUpdateRequest request
    ) {
        prodottoService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    /**
     * Ricerca paginata di prodotti applicando filtri.
     */
    @PostMapping("/search")
    public ResponseEntity<Page<ProdottoResponse>> searchProdotto(
            @Valid @RequestBody ProdottoSearchRequest searchRequest
    ) {
        return ResponseEntity.ok(prodottoService.search(searchRequest));
    }
}
