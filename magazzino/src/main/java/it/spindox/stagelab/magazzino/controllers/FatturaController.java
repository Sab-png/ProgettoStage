
package it.spindox.stagelab.magazzino.controllers;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaSearchRequest;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;



@RestController
@RequestMapping("/fatture")
@RequiredArgsConstructor
@Validated
public class FatturaController {

    private final FatturaService fatturaService;

    /**
     * GET /fatture       → solo ID (già implementato)
     * GET /fatture/list  → fatture complete (stream), paginato
     */

    @GetMapping
    public ResponseEntity<Page<Long>> getFattureIds(
            @RequestParam(required = false) String numero,
            @RequestParam(required = false) Long idProdotto,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFrom,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataTo,
            @RequestParam(required = false) BigDecimal importoMin,
            @RequestParam(required = false) BigDecimal importoMax,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size
    ) {

        FatturaSearchRequest req = new FatturaSearchRequest();
        req.setNumero(numero);
        req.setIdProdotto(idProdotto);
        req.setDataFrom(dataFrom);
        req.setDataTo(dataTo);
        req.setImportoMin(importoMin);
        req.setImportoMax(importoMax);
        req.setPage(page);
        req.setSize(size);

        Page<Long> ids = fatturaService.searchIds(req);
        return ResponseEntity.ok(ids);
    }

    /**
     * NEW: GET /fatture/list
     * Ritorna fatture COMPLETE, paginazione + stream
     */
    @GetMapping("/list")
    public ResponseEntity<Page<FatturaResponse>> getAllFatturePaged(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size
    ) {
        return ResponseEntity.ok(fatturaService.getAllPaged(page, size));
    }

    /**
     * GET /fatture/{id}
     */
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
            @Valid @RequestBody FatturaSearchRequest searchRequest
    ) {
        Page<FatturaResponse> page = fatturaService.search(searchRequest);
        return ResponseEntity.ok(page);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFattura(@PathVariable Long id) {
        fatturaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}