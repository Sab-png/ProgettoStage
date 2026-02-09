package it.spindox.stagelab.magazzino.dto.fattura;
import jakarta.validation.constraints.Min;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class FatturaSearchRequest {

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
