package it.spindox.stagelab.magazzino.controllers;

import it.spindox.stagelab.magazzino.dto.request.MagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.request.MagazzinoSearchRequest;
import it.spindox.stagelab.magazzino.dto.response.MagazzinoResponse;
import it.spindox.stagelab.magazzino.services.MagazzinoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/magazzini")
public class MagazzinoController {

    private final MagazzinoService service;

    public MagazzinoController(MagazzinoService service) {
        this.service = service;
    }

    // GET getMagazzino
    @GetMapping("/{id}")
    public ResponseEntity<MagazzinoResponse> getMagazzino(@PathVariable Long id) {
        return ResponseEntity.ok(service.getMagazzino(id));
    }

    // GET getMagazzinoByProdotto
    @GetMapping("/prodotto/{prodottoId}")
    public ResponseEntity<MagazzinoResponse> getMagazzinoByProdotto(
            @PathVariable Long prodottoId) {
        return ResponseEntity.ok(service.getMagazzinoByProdotto(prodottoId));
    }

    // POST saveMagazzino
    @PostMapping
    public ResponseEntity<MagazzinoResponse> saveMagazzino(
            @Valid @RequestBody MagazzinoRequest request) {
        return ResponseEntity.ok(service.saveMagazzino(request));
    }

    // POST searchMagazzino
    @PostMapping("/search")
    public ResponseEntity<Page<MagazzinoResponse>> searchMagazzino(
            @Valid @RequestBody MagazzinoSearchRequest request) {
        return ResponseEntity.ok(service.searchMagazzino(request));
    }
}
