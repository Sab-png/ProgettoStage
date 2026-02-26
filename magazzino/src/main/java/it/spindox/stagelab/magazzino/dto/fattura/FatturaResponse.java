
package it.spindox.stagelab.magazzino.dto.fattura;
import it.spindox.stagelab.magazzino.entities.SXFatturaStatus;
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

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataScadenza;

    // Importo pagato

    private BigDecimal pagato;

    private SXFatturaStatus status;

    // Opzionali per la visualizzazione

    private String statusDescription;
}