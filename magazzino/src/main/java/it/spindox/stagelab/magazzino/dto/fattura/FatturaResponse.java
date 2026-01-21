
package it.spindox.stagelab.magazzino.dto.fattura;
import jakarta.validation.constraints.Positive;import lombok.Data;
import java.math.BigDecimal;import java.time.LocalDate;

@Data
public class FatturaResponse {

    private Long id;
    private String numero;
    private LocalDate dataFattura;
    private Double importo;
    private Integer quantita;
    private Long idProdotto;
public void setImporto(@Positive BigDecimal importo) {}}
