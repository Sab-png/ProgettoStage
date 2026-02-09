
package it.spindox.stagelab.magazzino.dto.fattura;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class FatturaResponse {

    private Long id;
    private String numero;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataFattura;

    private BigDecimal importo;
    private Integer quantita;
    private Long idProdotto;
}