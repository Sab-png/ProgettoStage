package it.spindox.stagelab.magazzino.dto.prodotto;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ProdottoSearchRequest {

    // Filtro per nome del prodotto (opzionale)
    private String nome;

    // Filtro per prezzo minimo (opzionale)
    private Double prezzoMin;

    // Filtro per prezzo massimo (opzionale)
    private Double prezzoMax;

    // Numero della pagina per la paginazione (>= 0)
    @Min(value = 0, message = "La pagina deve essere >= 0")
    private int page = 0;

    // Numero di elementi per pagina (>= 1)
    @Min(value = 1, message = "La dimensione deve essere >= 1")
    private int size = 10;
}
