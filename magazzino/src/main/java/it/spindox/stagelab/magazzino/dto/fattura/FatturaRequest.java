
package it.spindox.stagelab.magazzino.dto.fattura;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;




@Data
@NoArgsConstructor
@AllArgsConstructor
public class FatturaRequest {

    //  Utente che crea la fattura
    @Size(max = 100, message = "Lo username non può superare 100 caratteri")
    private String username;

    //  Data della fattura
    @NotNull(message = "La data fattura è obbligatoria")
    private LocalDate dataFattura;

    //  Prodotto associato
    @NotNull(message = "Il prodotto è obbligatorio")
    private Long idProdotto;

    // Quantità
    @NotNull(message = "La quantità è obbligatoria")
    @Positive(message = "La quantità deve essere maggiore di zero")
    private Integer quantita;

    // Importo totale
    @NotNull(message = "L'importo è obbligatorio")
    @Positive(message = "L'importo deve essere maggiore di zero")
    private BigDecimal importo;

    // Data di scadenza
    private LocalDate dataScadenza;

    //  Costruttore  utile ai  mock
    public FatturaRequest(LocalDate dataFattura,
                          Long idProdotto,
                          Integer quantita,
                          BigDecimal importo,
                          LocalDate dataScadenza) {

        this.username = "system"; // oppure "system" se si vuole un default automatico
        this.dataFattura = dataFattura;
        this.idProdotto = idProdotto;
        this.quantita = quantita;
        this.importo = importo;
        this.dataScadenza = dataScadenza;
    }
}