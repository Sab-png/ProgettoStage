package it.spindox.stagelab.magazzino.dto.fattura;

import jakarta.validation.constraints.Min;
import lombok.Data;
import java.time.LocalDate;

@Data
public class FatturaSearchRequest {

    // Filtro data minima (opzionale)
    private LocalDate dataDa;

    // Filtro data massima (opzionale)
    private LocalDate dataA;

    // Filtro importo minimo (opzionale)
    private Double importoMin;

    // Filtro importo massimo (opzionale)
    private Double importoMax;

    // Numero pagina per la paginazione
    @Min(value = 0, message = "La pagina deve essere >= 0")
    private int page = 0;

    // Numero elementi per pagina
    @Min(value = 1, message = "La dimensione deve essere >= 1")
    private int size = 10;
}
