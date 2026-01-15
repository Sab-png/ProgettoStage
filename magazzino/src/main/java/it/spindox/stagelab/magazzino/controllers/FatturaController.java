package it.spindox.stagelab.magazzino.controllers;
import org.springframework.web.bind.annotation.*;// Spring lo usa per collegare una richiesta HTTP ai parametri del metodo nel controller.es:
//es:@ Path Variable,@ Request Param
import jakarta.validation.Valid;//controlla automaticamente i dati in ingresso e migliora la sicurezza delle API

@RestController
@RequestMapping("/fatture")
public class FatturaController {

    // Recupera una fattura dato il suo ID
    @GetMapping("/{id}")
    public void getFattura(@PathVariable Long id) {}

    // Recupera tutte le fatture associate a un prodotto
    @GetMapping("/prodotto/{idProdotto}")
    public void getFattureByProdotto(@PathVariable Long idProdotto) {}

    // Salva una nuova fattura (richiede dati validati)
    @PostMapping
    public void saveFattura(@Valid @RequestBody Object request) {}

    // Modifica una fattura esistente dato il suo ID
    @PatchMapping("/{id}")
    public void editFattura(@PathVariable Long id, @Valid @RequestBody Object request) {}

    // Ricerca paginata di fatture con filtri
    @PostMapping("/search")
    public void searchFattura(@Valid @RequestBody Object searchRequest) {}
}
