package it.spindox.stagelab.magazzino.controllers;

import it.spindox.stagelab.magazzino.dto.request.FatturaRequest;
import it.spindox.stagelab.magazzino.dto.request.FatturaSearchRequest;
import it.spindox.stagelab.magazzino.dto.response.FatturaResponse;
import it.spindox.stagelab.magazzino.services.FatturaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fatture")
public class FatturaController {

    
    private final FatturaService service;

    public FatturaController(FatturaService service) {
        this.service = service;
    }

    // GET getFattura
    @GetMapping("/{id}")
    public ResponseEntity<FatturaResponse> getFattura(@PathVariable Long id) {
        return ResponseEntity.ok(service.getFattura(id));
    }

    // GET getFattureByProdotto
    @GetMapping("/prodotto/{prodottoId}")
    public ResponseEntity<List<FatturaResponse>> getFattureByProdotto(
            @PathVariable Long prodottoId) {
        return ResponseEntity.ok(service.getFattureByProdotto(prodottoId));
    }

    // POST saveFattura
    @PostMapping
    public ResponseEntity<FatturaResponse> saveFattura(
            @Valid @RequestBody FatturaRequest request) {
        return ResponseEntity.ok(service.saveFattura(request));
    }

    // PATCH editFattura
    @PatchMapping("/{id}")
    public ResponseEntity<FatturaResponse> editFattura(
            @PathVariable Long id,
            @RequestBody FatturaRequest request) {
        return ResponseEntity.ok(service.editFattura(id, request));
    }

    // POST searchFattura
    @PostMapping("/search")
    public ResponseEntity<Page<FatturaResponse>> searchFattura(
            @Valid @RequestBody FatturaSearchRequest request) {
        return ResponseEntity.ok(service.searchFattura(request));
    }
}
