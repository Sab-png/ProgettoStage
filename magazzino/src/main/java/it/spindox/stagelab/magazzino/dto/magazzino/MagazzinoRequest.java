
package it.spindox.stagelab.magazzino.dto.magazzino;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor

public class MagazzinoRequest {

    private String nome;
    private String indirizzo;

    @Positive(message = "La capacità deve essere maggiore di zero")
    private Integer capacita;

    // Filtri ricerca
    private Integer capacitaMin;
    private Integer capacitaMax;

    // Paginazione
    @Min(0)
    private Integer page = 0;

    @Min(1)
    private Integer size = 10;

    }

