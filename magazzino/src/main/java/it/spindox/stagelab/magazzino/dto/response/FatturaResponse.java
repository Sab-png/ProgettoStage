package it.spindox.stagelab.magazzino.dto.response;

import lombok.Data;
import java.time.LocalDate;

@Data
public class FatturaResponse {

    private Long id;
    private LocalDate dataFattura;
    private Double importo;
    private Long prodottoId;
}
