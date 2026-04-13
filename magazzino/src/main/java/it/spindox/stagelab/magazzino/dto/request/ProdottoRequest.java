package it.spindox.stagelab.magazzino.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProdottoRequest {

    @NotBlank(message = "Nome obbligatorio")
    private String nome;

    private String descrizione;

    @NotNull(message = "Prezzo obbligatorio")
    @Positive(message = "Prezzo deve essere positivo")
    private Double prezzo;

    // getter e setter
}

