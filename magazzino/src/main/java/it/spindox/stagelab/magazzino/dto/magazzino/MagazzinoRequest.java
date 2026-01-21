
package it.spindox.stagelab.magazzino.dto.magazzino;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class MagazzinoRequest {

    // Nome magazzino
    private String nome;

    // Indirizzo
    private String indirizzo;

    // Capacità
    @Positive(message = "La capacità deve essere maggiore di zero")
    private Integer capacita;

    // Campi SEARCH
    private Integer capacitaMin;
    private Integer capacitaMax;

    // Paginazione
    @Min(0)
    private int page = 0;

    @Min(1)
    private int size = 10;

    public Object getCodice() {
        return null;
    }
}


