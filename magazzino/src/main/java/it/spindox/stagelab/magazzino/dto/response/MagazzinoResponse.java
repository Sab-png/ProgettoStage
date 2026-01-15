package it.spindox.stagelab.magazzino.dto.response;

import lombok.Data;

@Data
public class MagazzinoResponse {

    private Long id;
    private String nome;
    private String indirizzo;
    private Integer capacita;

}
