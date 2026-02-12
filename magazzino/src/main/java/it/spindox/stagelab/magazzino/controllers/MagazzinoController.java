
package it.spindox.stagelab.magazzino.controllers;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoResponse;
import it.spindox.stagelab.magazzino.services.MagazzinoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/magazzino")
@RequiredArgsConstructor
@Validated
public class MagazzinoController {

    private final MagazzinoService magazzinoService;

    // GET ALL → solo ID
    @GetMapping
    public ResponseEntity<Page<Long>> getIds(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String indirizzo,
            @RequestParam(required = false) Integer capacitaMin,
            @RequestParam(required = false) Integer capacitaMax,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        MagazzinoRequest req = new MagazzinoRequest();
        req.setNome(nome);
        req.setIndirizzo(indirizzo);
        req.setCapacitaMin(capacitaMin);
        req.setCapacitaMax(capacitaMax);
        req.setPage(page);
        req.setSize(size);

        return ResponseEntity.ok(magazzinoService.searchIds(req));
    }

    // GET ALL DTO COMPLETI + stream
    @GetMapping("/list")
    public ResponseEntity<Page<MagazzinoResponse>> getAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(magazzinoService.getAllPaged(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MagazzinoResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(magazzinoService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody MagazzinoRequest request) {
        magazzinoService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @Valid @RequestBody MagazzinoRequest request) {
        magazzinoService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        magazzinoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/search")
    public ResponseEntity<Page<MagazzinoResponse>> search(
            @Valid @RequestBody MagazzinoRequest request) {
        return ResponseEntity.ok(magazzinoService.search(request));
    }
}