package it.spindox.stagelab.magazzino.dto.magazzino;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class MagazzinoSearchRequest {

    // Filtri opzionali
    private String nome;
    private String indirizzo;
    private Integer capacitaMin;
    private Integer capacitaMax;

    // Paginazione
    @Min(0)
    private Integer page = 0;

    @Min(1)
    private Integer size = 10;
}