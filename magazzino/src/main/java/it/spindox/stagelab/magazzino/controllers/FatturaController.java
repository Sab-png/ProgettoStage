
package it.spindox.stagelab.magazzino.controllers;
import it.spindox.stagelab.magazzino.dto.FatturaWorkExecution.DtoPaymentRequest;
import it.spindox.stagelab.magazzino.dto.FatturaWorkExecution.DtoPaymentResponse;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaRequest;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaSearchRequest;
import it.spindox.stagelab.magazzino.services.FatturaService;
import it.spindox.stagelab.magazzino.services.FatturaWorkExecutionService;
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
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;






@RestController
@RequestMapping("/fatture")
@RequiredArgsConstructor
@Validated
public class FatturaController {

    private final FatturaService fatturaService;
    private final FatturaWorkExecutionService fatturaWorkExecutionService;

    // GET /fatture by IDs

    @GetMapping
    public ResponseEntity<Page<Long>> getFattureIds(
            @RequestParam(required = false) String numero,
            @RequestParam(required = false) Long idProdotto,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataTo,
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

    // GET /fatture/list : fatture complete paginato

    @GetMapping("/list")
    public ResponseEntity<Page<FatturaResponse>> getAllFatturePaged(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size
    ) {
        return ResponseEntity.ok(fatturaService.getAllPaged(page, size));
    }


    // GET /fatture/id

    @GetMapping("/{id}")
    public ResponseEntity<FatturaResponse> getFattura(@PathVariable Long id) {
        return ResponseEntity.ok(fatturaService.getById(id));
    }


    // GET /fatture/prodotto/idProdotto

    @GetMapping("/prodotto/{idProdotto}")
    public ResponseEntity<PageImpl<FatturaResponse>> getFattureByProdotto(
            @PathVariable Long idProdotto,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size
    ) {
        PageImpl<FatturaResponse> result = fatturaService.getByProdotto(idProdotto, page, size);
        return ResponseEntity.ok(result);
    }


    // POST /fatture : create

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


    // Fatture testing

    // FATTURA EMESSA

    @PostMapping("/status/emessa")
    public ResponseEntity<FatturaResponse> statusEmessa(
            @RequestParam Long idProdotto,
            @RequestBody(required = false) FatturaRequest body
    ) {
        FatturaRequest req = (body != null) ? body : new FatturaRequest();
        req.setIdProdotto(idProdotto);

        if (req.getDataFattura() == null) req.setDataFattura(LocalDate.now());
        if (req.getQuantita() == null) req.setQuantita(1);
        if (req.getImporto() == null) req.setImporto(new BigDecimal("1.00"));
        if (req.getDataScadenza() == null) req.setDataScadenza(LocalDate.now().plusDays(30));
        if (req.getUsername() == null || req.getUsername().isBlank()) req.setUsername("system");

        return ResponseEntity.ok(fatturaService.createMockEmessa(req));
    }


    //  FATTURA SCADUTA

    @PostMapping("/status/scaduta")
    public ResponseEntity<FatturaResponse> statusScaduta(
            @RequestParam Long idProdotto,
            @RequestBody(required = false) FatturaRequest body
    ) {
        FatturaRequest req = (body != null) ? body : new FatturaRequest();
        req.setIdProdotto(idProdotto);

        if (req.getDataFattura() == null) req.setDataFattura(LocalDate.now());
        if (req.getQuantita() == null) req.setQuantita(1);
        if (req.getImporto() == null) req.setImporto(new BigDecimal("1.00"));
        if (req.getDataScadenza() == null) req.setDataScadenza(LocalDate.now().minusDays(30));
        if (req.getUsername() == null || req.getUsername().isBlank()) req.setUsername("system");

        return ResponseEntity.ok(fatturaService.createMockScaduta(req));
    }


    // FATTURA PAGATA

    @PostMapping("/status/pagata")
    public ResponseEntity<FatturaResponse> statusPagata(
            @RequestParam Long idProdotto,
            @RequestBody(required = false) FatturaRequest body
    ) {
        FatturaRequest req = (body != null) ? body : new FatturaRequest();
        req.setIdProdotto(idProdotto);

        if (req.getDataFattura() == null) req.setDataFattura(LocalDate.now());
        if (req.getQuantita() == null) req.setQuantita(1);
        if (req.getImporto() == null) req.setImporto(new BigDecimal("1.00"));
        if (req.getDataScadenza() == null) req.setDataScadenza(LocalDate.now().plusDays(30));
        if (req.getUsername() == null || req.getUsername().isBlank()) req.setUsername("system");

        return ResponseEntity.ok(fatturaService.createMockPagata(req));
    }

   // PATCH /fatture/{id} : update generico

    @PatchMapping("/{id}")
    public ResponseEntity<FatturaResponse> editFattura(
            @PathVariable Long id,
            @Valid @RequestBody FatturaRequest request
    ) {
        FatturaResponse updated = fatturaService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    // PATCH pagamento singola fattura

    @PatchMapping(
            path = "/{id}/payment-checkfattura",
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<DtoPaymentResponse> paymentCheckFattura(
            @PathVariable Long id,
            @Valid @RequestBody DtoPaymentRequest request
    ) {
        if (request == null || request.getPagatoDaAggiungere() == null) {
            return ResponseEntity.badRequest().build();
        }

        DtoPaymentResponse updated = fatturaWorkExecutionService.paymentCheckFattura(
                id,
                request.getPagatoDaAggiungere()
        );
        return ResponseEntity.ok(updated);
    }


    // POST check pagamento di tutte le fatture

    // POST /payment-check-all


    @PostMapping("/payment-check-all")
    public ResponseEntity<Map<String, Object>> checkAllFatture() {

        List<DtoPaymentResponse> items =
                fatturaWorkExecutionService.paymentCheckAllFatture();

        Map<String, Object> body = Map.of(
                "updatedCount", fatturaWorkExecutionService.getLastUpdatedCount(),
                "items", items
        );             // lista dei problemi


        return ResponseEntity.ok(body);
    }



    // POST /fatture/search

    @PostMapping("/search")
    public ResponseEntity<Page<FatturaResponse>> searchFattura(
            @Valid @RequestBody FatturaSearchRequest searchRequest
    ) {
        Page<FatturaResponse> page = fatturaService.search(searchRequest);
        return ResponseEntity.ok(page);
    }

    // DELETE /fatture/id

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFattura(@PathVariable Long id) {
        fatturaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


