package it.spindox.stagelab.magazzino.controllers;

import it.spindox.stagelab.magazzino.dto.request.ProdottoRequest;
import it.spindox.stagelab.magazzino.dto.request.ProdottoSearchRequest;
import it.spindox.stagelab.magazzino.dto.response.ProdottoResponse;
import it.spindox.stagelab.magazzino.services.ProdottoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prodotti")
public class ProdottoController {

    private final ProdottoService service;

    public ProdottoController(ProdottoService service) {
        this.service = service;
    }

    // GET getProdotto
    @GetMapping("/{id}")
    public ResponseEntity<ProdottoResponse> getProdotto(@PathVariable Long id) {
        return ResponseEntity.ok(service.getProdotto(id));
    }

    // POST saveProdotto
    @PostMapping
    public ResponseEntity<ProdottoResponse> saveProdotto(
            @Valid @RequestBody ProdottoRequest request) {
        return ResponseEntity.ok(service.saveProdotto(request));
    }

    // PATCH editProdotto
    @PatchMapping("/{id}")
    public ResponseEntity<ProdottoResponse> editProdotto(
            @PathVariable Long id,
            @RequestBody ProdottoRequest request) {
        return ResponseEntity.ok(service.editProdotto(id, request));
    }

    // POST searchProdotto
    @PostMapping("/search")
    public ResponseEntity<Page<ProdottoResponse>> searchProdotto(
            @Valid @RequestBody ProdottoSearchRequest request) {
        return ResponseEntity.ok(service.searchProdotto(request));
    }
}
