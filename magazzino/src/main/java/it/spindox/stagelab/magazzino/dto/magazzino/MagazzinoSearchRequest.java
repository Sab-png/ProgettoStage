package it.spindox.stagelab.magazzino.dto.magazzino;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class MagazzinoSearchRequest {

    // Filtro per nome del magazzino (opzionale)
    private String nome;

    // Filtro capacità minima (opzionale)
    private Integer capacitaMin;

    // Filtro capacità massima (opzionale)
    private Integer capacitaMax;

    // Numero pagina per la paginazione
    @Min(value = 0, message = "La pagina deve essere >= 0")
    private int page = 0;

    // Numero elementi per pagina
    @Min(value = 1, message = "La dimensione deve essere >= 1")
    private int size = 10;
}
