
package it.spindox.stagelab.magazzino.dto.prodotto;
import lombok.Data;import java.math.BigDecimal;

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
public void setPrezzo(BigDecimal prezzo) {}}
