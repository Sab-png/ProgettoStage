package it.spindox.stagelab.magazzino.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddToCartRequest {

    @NotNull(message = "Id prodotto obbligatorio")
    private Long prodottoId;

    @NotNull(message = "Quantità obbligatoria")
    @Min(value = 1, message = "Quantità minima è 1")
    private Integer quantity;
}