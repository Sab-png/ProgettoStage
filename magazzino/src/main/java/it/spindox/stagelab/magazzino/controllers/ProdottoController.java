
package it.spindox.stagelab.magazzino.controllers;
import it.spindox.stagelab.magazzino.dto.prodotto.ProdottoRequest;
import it.spindox.stagelab.magazzino.dto.prodotto.ProdottoResponse;
import it.spindox.stagelab.magazzino.services.ProdottoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/prodotti")
@RequiredArgsConstructor
public class ProdottoController {

    private final ProdottoService prodottoService;

    /**
     * NEW: GET /prodotti
     * Ritorna SOLO gli ID dei prodotti filtrati (Page<Long>)
     */
    @GetMapping
    public ResponseEntity<Page<Long>> getProdottiIds(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String categoria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ProdottoRequest req = new ProdottoRequest();
        req.setNome(nome);
        req.setCategoria(categoria);
        req.setPage(page);
        req.setSize(size);

        Page<Long> ids = prodottoService.searchIds(req);
        return ResponseEntity.ok(ids);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdottoResponse> getProdotto(@PathVariable Long id) {
        return ResponseEntity.ok(prodottoService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Void> saveProdotto(@Valid @RequestBody ProdottoRequest request) {
        prodottoService.create(request);
        return ResponseEntity.status(201).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> editProdotto(
            @PathVariable Long id,
            @Valid @RequestBody ProdottoRequest request
    ) {
        prodottoService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProdotto(@PathVariable Long id) {
        prodottoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/search")
    public ResponseEntity<Page<ProdottoResponse>> searchProdotto(
            @Valid @RequestBody ProdottoRequest searchRequest
    ) {
        return ResponseEntity.ok(prodottoService.search(searchRequest));
    }
}