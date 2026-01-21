
package it.spindox.stagelab.magazzino.dto.prodotto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProdottoRequest {

    // Nome del prodotto
    private String nome;

    // Descrizione del prodotto
    private String descrizione;

    // Prezzo del prodotto
    @Positive(message = "Il prezzo deve essere maggiore di zero")
    private Double prezzo;

    // Campi SEARCH opzionali
    private Double prezzoMin;
    private Double prezzoMax;

    // Paginazione
    @Min(0)
    private int page = 0;

    @Min(1)
    private int size = 10;
public String getCategoria() {
    return "";
}

    public String getCodice() {
        return "";
    }
}
