package it.spindox.stagelab.magazzino.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Data;
import java.time.LocalDate;

@Data
public class FatturaSearchRequest {

    private LocalDate dataDa;
    private LocalDate dataA;
    private Double importoMin;
    private Double importoMax;

    @Min(0)
    private int page;

    @Min(1)
    private int size;
}
