package it.spindox.stagelab.magazzino.controllers;
import org.springframework.web.bind.annotation.*;// Spring lo usa per collegare una richiesta HTTP ai parametri del metodo nel controller.es:
//es:@ Path Variable,@ Request Param
import jakarta.validation.Valid;//controlla automaticamente i dati in ingresso e migliora la sicurezza delle API

@RestController
@RequestMapping("/magazzini")
public class MagazzinoController {

    // Recupera un magazzino dato il suo ID
    @GetMapping("/{id}")
    public void getMagazzino(@PathVariable Long id) {}

    // Recupera un magazzino associato a un prodotto
    @GetMapping("/prodotto/{idProdotto}")
    public void getMagazzinoByProdotto(@PathVariable Long idProdotto) {}

    // Salva un nuovo magazzino
    @PostMapping
    public void saveMagazzino(@Valid @RequestBody Object request) {}

    // Ricerca paginata di magazzini con filtri
    @PostMapping("/search")
    public void searchMagazzino(@Valid @RequestBody Object searchRequest) {}
}
