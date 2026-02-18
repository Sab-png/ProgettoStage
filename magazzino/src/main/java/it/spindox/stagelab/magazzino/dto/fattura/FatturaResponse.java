
package it.spindox.stagelab.magazzino.dto.fattura;
import it.spindox.stagelab.magazzino.entities.SXFatturaStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class FatturaResponse {

    private Long id;
    private String numero;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataFattura;

    private BigDecimal importo;
    private Integer quantita;
    private Long idProdotto;

    private LocalDate dataScadenza;
    private Boolean pagato;
    private SXFatturaStatus status;
    private String statusDescription;

    public void setDataScadenza(Object dataScadenza) {
        log.info("Impostata dataScadenza: {}", dataScadenza);
        if (dataScadenza instanceof LocalDate) {
            this.dataScadenza = (LocalDate) dataScadenza;
        }
    }

    public void setPagato(Object pagato) {
        log.info("pagato: {}", pagato);
        if (pagato instanceof Boolean) {
            this.pagato = (Boolean) pagato;
        }
    }

    public void setStatus(SXFatturaStatus status) {
        log.info("status impostato : {}", status);
        this.status = status;
    }

    public void setStatusDescription(String description) {
        log.info("Impostata statusDescription: {}", description);
        this.statusDescription = description;
    }
}