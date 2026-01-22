
package it.spindox.stagelab.magazzino.controllers;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;
import it.spindox.stagelab.magazzino.services.FatturaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/fatture")
@RequiredArgsConstructor
@Validated
public class FatturaController {

    private final FatturaService fatturaService;

    @GetMapping("/{id}")
    public ResponseEntity<FatturaResponse> getFattura(@PathVariable Long id) throws Throwable {
        return ResponseEntity.ok(fatturaService.getById(id));
    }

    @GetMapping("/prodotto/{idProdotto}")
    public ResponseEntity<PageImpl<FatturaResponse>> getFattureByProdotto(
            @PathVariable Long idProdotto,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size
    ) {
        PageImpl<FatturaResponse> result = fatturaService.getByProdotto(idProdotto, page, size);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<FatturaResponse> saveFattura(
            @Valid @RequestBody FatturaRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        FatturaResponse created = fatturaService.create(request);
        URI location = uriBuilder.path("/fatture/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FatturaResponse> editFattura(
            @PathVariable Long id,
            @Valid @RequestBody FatturaRequest request
    ) throws Throwable {
        FatturaResponse updated = fatturaService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<FatturaResponse>> searchFattura(
            @Valid @RequestBody FatturaRequest searchRequest
    ) {
        Page<FatturaResponse> page = (Page<FatturaResponse>) fatturaService.search(searchRequest);
        return ResponseEntity.ok(page);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFattura(@PathVariable Long id) {
        fatturaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
