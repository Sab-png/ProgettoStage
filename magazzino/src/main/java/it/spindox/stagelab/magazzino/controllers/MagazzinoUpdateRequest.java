package it.spindox.stagelab.magazzino.controllers;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class MagazzinoUpdateRequest {

    @Min(0)
    private Integer quantita;

    private String ubicazione;
}

