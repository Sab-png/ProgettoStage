package it.spindox.stagelab.magazzino.dto.fattura;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;



@Data
@NoArgsConstructor
@AllArgsConstructor

public class FatturaSearchRequest {

    private String numero;
    private Long idProdotto;
    private LocalDate dataFrom;
    private LocalDate dataTo;
    private BigDecimal importoMin;
    private BigDecimal importoMax;

    @Min(0)
    private int page = 0;

    @Min(1)
    private int size = 10;
}

