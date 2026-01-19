package it.spindox.stagelab.magazzino.dto.prodotto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProdottoCreateRequest {

    // Nome del prodotto – obbligatorio e non può essere vuoto
    @NotBlank(message = "Il nome è obbligatorio")
    private String nome;

    // Descrizione del prodotto – obbligatoria e non può essere vuota
    @NotBlank(message = "La descrizione è obbligatoria")
    private String descrizione;

    // Prezzo del prodotto – obbligatorio e deve essere maggiore di zero
    @NotNull(message = "Il prezzo è obbligatorio")
    @Positive(message = "Il prezzo deve essere maggiore di zero")
    private Double prezzo;
}
