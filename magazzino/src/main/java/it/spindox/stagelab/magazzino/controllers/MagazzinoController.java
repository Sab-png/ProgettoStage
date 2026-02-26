
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
import it.spindox.stagelab.magazzino.dto.magazzino.MagazzinoSearchRequest;
import jakarta.validation.constraints.Min;




@RestController
@RequestMapping("/magazzino")
@RequiredArgsConstructor
@Validated
public class MagazzinoController {

    private final MagazzinoService magazzinoService;


    // GET /magazzino  : Restituisce SOLO ID


    @GetMapping
    public ResponseEntity<Page<Long>> getIds(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String indirizzo,
            @RequestParam(required = false) Integer capacitaMin,
            @RequestParam(required = false) Integer capacitaMax,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size
    ) {
        MagazzinoSearchRequest req = new MagazzinoSearchRequest(
                nome, indirizzo, capacitaMin, capacitaMax, page, size
        );

        return ResponseEntity.ok(magazzinoService.searchIds(req));
    }


    // GET /magazzino/list : Tutti i magazzini completi (DTO)


    @GetMapping("/list")
    public ResponseEntity<Page<MagazzinoResponse>> getAllPaged(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "8") @Min(1) int size
    ) {
        return ResponseEntity.ok(magazzinoService.getAllPaged(page, size));
    }


    // GET /magazzino/{id} : singolo magazzino per ID (DTO)


    @GetMapping("/{id}")
    public ResponseEntity<MagazzinoResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(magazzinoService.getById(id));
    }


    // POST /magazzino  → CREATE


    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody MagazzinoRequest request) {
        magazzinoService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    // PATCH /magazzino/{id}  → UPDATE


    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @Valid @RequestBody MagazzinoRequest request
    ) {
        magazzinoService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    // DELETE /magazzino/{id}  : DELETE


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        magazzinoService.delete(id);
        return ResponseEntity.noContent().build();
    }


    // POST /magazzino/search  : Ricerca completa con DTO dedicato


    @PostMapping("/search")
    public ResponseEntity<Page<MagazzinoResponse>> search(
            @Valid @RequestBody MagazzinoSearchRequest request
    ) {
        return ResponseEntity.ok(magazzinoService.search(request));
    }
}