
package it.spindox.stagelab.magazzino.dto.fattura;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class FatturaResponse {

    private Long id;
    private String numero;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataFattura;
    private BigDecimal importo;
    private Integer quantita;
    private Long idProdotto;
}