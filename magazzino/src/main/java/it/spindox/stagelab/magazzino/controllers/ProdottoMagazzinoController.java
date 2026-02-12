package it.spindox.stagelab.magazzino.controllers;
import it.spindox.stagelab.magazzino.dto.ProdottoMagazzino.ProdottoMagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.ProdottoMagazzino.ProdottoMagazzinoResponse;
import it.spindox.stagelab.magazzino.dto.ProdottoMagazzino.ProdottoMagazzinoSearchRequest;
import it.spindox.stagelab.magazzino.services.ProdottoMagazzinoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/prodotto-magazzino")
@RequiredArgsConstructor
public class ProdottoMagazzinoController {

    private final ProdottoMagazzinoService service;

    /**
     * NEW: GET /prodotto-magazzino
     * Ritorna SOLO gli ID filtrati (Page<Long>)
     */
    @GetMapping
    public ResponseEntity<Page<Long>> getIds(
            @RequestParam(required = false) Long prodottoId,
            @RequestParam(required = false) Long magazzinoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ProdottoMagazzinoSearchRequest req = new ProdottoMagazzinoSearchRequest();
        req.setProdottoId(prodottoId);
        req.setMagazzinoId(magazzinoId);
        req.setPage(page);
        req.setSize(size);

        Page<Long> ids = service.searchIds(req);
        return ResponseEntity.ok(ids);
    }

    // ------------------------------
    // CRUD
    // ------------------------------

    @GetMapping("/{id}")
    public ProdottoMagazzinoResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid ProdottoMagazzinoRequest request) {
        service.create(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id,
                       @RequestBody @Valid ProdottoMagazzinoRequest request) {
        service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // ------------------------------
    // SEARCH (DTO completi)
    // ------------------------------

    @PostMapping("/search")
    public Page<ProdottoMagazzinoResponse> search(
            @RequestBody @Valid ProdottoMagazzinoSearchRequest request
    ) {
        return service.search(request);
    }
}