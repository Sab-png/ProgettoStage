
package it.spindox.stagelab.magazzino.dto.prodotto;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor


public class ProdottoRequest {

    private String nome;          // filtro su nome
    private String descrizione;   // filtro su descrizione

    //  Prezzo singolo

    @Positive(message = "Il prezzo deve essere maggiore di zero")
    private BigDecimal prezzo;

    // Filtri di ricerca su range prezzo

    private BigDecimal prezzoMin;
    private BigDecimal prezzoMax;

    //  Paginazione

    @Min(value = 0, message = "La pagina deve essere >= 0")
    private Integer page;   // default: 0 nel Service

    @Min(value = 1, message = "La size deve essere >= 1")
    private Integer size;   // default: 20 nel Service

    // Validazione: prezzoMin <= prezzoMax quando entrambi presenti

    @AssertTrue(message = "prezzoMin non può essere maggiore di prezzoMax")
    public boolean isPrezzoRangeValido() {
        if (prezzoMin == null || prezzoMax == null) return true;
        return prezzoMin.compareTo(prezzoMax) <= 0;
    }

    public void setCategoria(String categoria) {

    }
}
