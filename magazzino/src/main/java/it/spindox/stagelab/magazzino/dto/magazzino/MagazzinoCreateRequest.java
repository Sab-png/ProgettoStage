package it.spindox.stagelab.magazzino.dto.magazzino;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class MagazzinoCreateRequest {

    // Nome del magazzino – obbligatorio
    @NotBlank(message = "Il nome è obbligatorio")
    private String nome;

    // Indirizzo fisico del magazzino – obbligatorio
    @NotBlank(message = "L'indirizzo è obbligatorio")
    private String indirizzo;

    // Capacità massima del magazzino – obbligatoria e > 0
    @NotNull(message = "La capacità è obbligatoria")
    @Positive(message = "La capacità deve essere maggiore di zero")
    private Integer capacita;
}
