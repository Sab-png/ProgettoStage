package it.spindox.stagelab.magazzino.controllers;
import org.springframework.web.bind.annotation.*;// Spring lo usa per collegare una richiesta HTTP ai parametri del metodo nel controller.es:
//es:@ Path Variable,@ Request Param
import jakarta.validation.Valid;//controlla automaticamente i dati in ingresso e migliora la sicurezza delle API

@RestController
@RequestMapping("/prodotti")
public class ProdottoController {

    // Recupera un prodotto dato il suo ID
    @GetMapping("/{id}")
    public void getProdotto(@PathVariable Long id) {}

    // Salva un nuovo prodotto
    @PostMapping
    public void saveProdotto(@Valid @RequestBody Object request) {}

    // Modifica un prodotto esistente dato il suo ID
    @PatchMapping("/{id}")
    public void editProdotto(@PathVariable Long id, @Valid @RequestBody Object request) {}

    // Ricerca paginata di prodotti con filtri
    @PostMapping("/search")
    public void searchProdotto(@Valid @RequestBody Object searchRequest) {}
}
