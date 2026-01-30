package it.spindox.stagelab.magazzino.controllers;
import it.spindox.stagelab.magazzino.dto.ProdottoMagazzino.ProdottoMagazzinoRequest;
import it.spindox.stagelab.magazzino.dto.ProdottoMagazzino.ProdottoMagazzinoResponse;
import it.spindox.stagelab.magazzino.services.ProdottoMagazzinoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prodotto-magazzino")
@RequiredArgsConstructor
public class ProdottoMagazzinoController<ProdottoMagazzinoSearchRequest> {

    private final ProdottoMagazzinoService service;
    // CRUD
    @GetMapping("/{id}")
    public ProdottoMagazzinoResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid ProdottoMagazzinoRequest request) {
        service.create(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id,
                       @RequestBody @Valid ProdottoMagazzinoRequest request) {
        service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }


    //  SEARCH

    /**
     * Esempio: POST /prodotto-magazzino/search
     * Body: { "prodottoId": 1, "magazzinoId": 2, "page": 0, "size": 20 }
     */
    @PostMapping("/search")
    public Page<ProdottoMagazzinoResponse> search(@RequestBody @Valid ProdottoMagazzinoSearchRequest request) {
        return service.search(request);
    }

}

