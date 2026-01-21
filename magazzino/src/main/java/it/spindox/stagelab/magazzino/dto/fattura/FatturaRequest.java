
package it.spindox.stagelab.magazzino.dto.fattura;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.time.LocalDate;

@Data
public class FatturaRequest {

    // Numero fattura
    private String numero;

    // Data fattura
    private LocalDate dataFattura;

    // Prodotto associato
    private Long idProdotto;

    // Quantità
    @Positive(message = "La quantità deve essere maggiore di zero")
    private Integer quantita;

    // Importo totale
    @Positive(message = "L'importo deve essere maggiore di zero")
    private Double importo;

    // Campi SEARCH
    private LocalDate dataFrom;
    private LocalDate dataTo;
    private Double importoMin;
    private Double importoMax;

    // Paginazione
    @Min(0)
    private int page = 0;

    @Min(1)
    private int size = 10;
}
