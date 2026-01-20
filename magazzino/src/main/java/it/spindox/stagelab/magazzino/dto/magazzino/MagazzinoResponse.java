package it.spindox.stagelab.magazzino.dto.magazzino;
import lombok.Data;

@Data
public class MagazzinoResponse {

    // Identificatione del magazzino
    private Long id;

    // Nome del magazzino
    private String nome;

    // Indirizzo del magazzino
    private String indirizzo;

    // Capacità Max
    private Integer capacita;

    public void setDescrizione(Object descrizione) {
    }
}
