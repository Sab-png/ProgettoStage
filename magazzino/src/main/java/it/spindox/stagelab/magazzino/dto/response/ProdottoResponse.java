package it.spindox.stagelab.magazzino.dto.response;

import lombok.Data;

@Data
public class ProdottoResponse {

    private Long id;
    private String nome;
    private String descrizione;
    private Double prezzo;

    // getter e setter
}

