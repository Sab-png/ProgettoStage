package it.spindox.stagelab.magazzino.dto.prodotto;
import lombok.Data;

@Data
public class ProdottoResponse {

    // Identificativo univoco del prodotto
    private Long id;

    // Nome del prodotto
    private String nome;

    // Descrizione del prodotto
    private String descrizione;

    // Prezzo del prodotto
    private Double prezzo;
}
