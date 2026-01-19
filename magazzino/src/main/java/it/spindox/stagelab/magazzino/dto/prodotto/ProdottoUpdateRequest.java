package it.spindox.stagelab.magazzino.dto.prodotto;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProdottoUpdateRequest {

    // Nome aggiornato del prodotto (opzionale)
    private String nome;

    // Descrizione aggiornata del prodotto (opzionale)
    private String descrizione;

    // Prezzo aggiornato – se presente deve essere > 0
    @Positive(message = "Il prezzo deve essere maggiore di zero")
    private Double prezzo;
}
