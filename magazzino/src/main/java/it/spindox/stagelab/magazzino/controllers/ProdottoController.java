
package it.spindox.stagelab.magazzino.controllers;
import it.spindox.stagelab.magazzino.dto.prodotto.ProdottoRequest;
import it.spindox.stagelab.magazzino.dto.prodotto.ProdottoResponse;
import it.spindox.stagelab.magazzino.services.ProdottoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;


@RestController
@RequestMapping("/prodotti")
@RequiredArgsConstructor
@Validated
public class ProdottoController {

    private final ProdottoService service;

    // GET ALL solo ID

    @GetMapping
    public ResponseEntity<Page<Long>> getIds(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String descrizione,
            @RequestParam(required = false) BigDecimal prezzoMin,
            @RequestParam(required = false) BigDecimal prezzoMax,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ProdottoRequest r = new ProdottoRequest();
        r.setNome(nome);
        r.setDescrizione(descrizione);
        r.setPrezzoMin(prezzoMin);
        r.setPrezzoMax(prezzoMax);
        r.setPage(page);
        r.setSize(size);

        return ResponseEntity.ok(service.searchIds(r));
    }

    // GET ALL paged + stream

    @GetMapping("/list")
    public ResponseEntity<Page<ProdottoResponse>> getAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(service.getAllPaged(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdottoResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody ProdottoRequest request) {
        service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @Valid @RequestBody ProdottoRequest request) {
        service.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/search")
    public ResponseEntity<Page<ProdottoResponse>> search(@RequestBody @Valid ProdottoRequest r) {
        return ResponseEntity.ok(service.search(r));
    }
}