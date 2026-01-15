package it.spindox.stagelab.magazzino.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class MagazzinoRequest {

    @NotBlank(message = "Nome obbligatorio")
    private String nome;

    @NotBlank(message = "Indirizzo obbligatorio")
    private String indirizzo;

    @NotNull(message = "Capacità obbligatoria")
    @PositiveOrZero(message = "Capacità non può essere negativa")
    private Integer capacita;
}

