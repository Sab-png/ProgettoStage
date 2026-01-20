
package it.spindox.stagelab.magazzino.controllers;
import it.spindox.stagelab.magazzino.dto.prodotto.*;
import it.spindox.stagelab.magazzino.services.ProdottoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST per la gestione dei Prodotti.
 */
@RestController
@RequestMapping("/prodotti")
@RequiredArgsConstructor
public class ProdottoController {

    private final ProdottoService prodottoService;

    /**
     * READ - Recupera un prodotto dato il suo ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProdottoResponse> getProdotto(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(prodottoService.getById(id));
    }

    /**
     * CREATE - Salva un nuovo prodotto.
     */
    @PostMapping
    public ResponseEntity<Void> saveProdotto(
            @Valid @RequestBody ProdottoRequest request
    ) {
        prodottoService.create(request);
        return ResponseEntity.status(201).build(); // CREATED
    }

    /**
     * UPDATE - Aggiornamento parziale (PATCH).
     * Vengono aggiornati solo i campi non null.
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
     * DELETE - Elimina un prodotto.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProdotto(
            @PathVariable Long id
    ) {
        prodottoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * READ (SEARCH) - Ricerca paginata di prodotti applicando filtri.
     */
    @PostMapping("/search")
    public ResponseEntity<Page<ProdottoResponse>> searchProdotto(
            @Valid @RequestBody ProdottoSearchRequest searchRequest
    ) {
        return ResponseEntity.ok(prodottoService.search(searchRequest));
    }
}
