package it.spindox.stagelab.magazzino.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ProdottoSearchRequest {

    private String nome;
    private Double prezzoMin;
    private Double prezzoMax;

    @Min(0)
    private int page;

    @Min(1)
    private int size;
}
