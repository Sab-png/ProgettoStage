package it.spindox.stagelab.magazzino.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class MagazzinoSearchRequest {

    private String nome;
    private String indirizzo;

    @Min(0)
    private int page;

    @Min(1)
    private int size;
}

