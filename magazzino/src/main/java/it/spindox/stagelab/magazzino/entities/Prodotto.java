package it.spindox.stagelab.magazzino.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "PRODOTTO")
@SequenceGenerator(
        name = "prodotto_seq_gen",
        sequenceName = "PRODOTTO_SEQ",
        allocationSize = 1
)
public class Prodotto {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "prodotto_seq_gen"
    )
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "DESCRIZIONE")
    private String descrizione;

    @Column(name = "PREZZO")
    private Double prezzo;

    @OneToOne(mappedBy = "prodotto")
    private Fattura fattura;

    @OneToMany(mappedBy = "prodotto")
    private List<ProdottoMagazzino> prodottoMagazzino;

    @Column(name = "TOTAL_STOCK", nullable = false)
    private Integer totalStock; // Stock totale fisico

    @Column(name = "AVAILABLE_STOCK", nullable = false)
    private Integer availableStock; // Stock disponibile (totale - riservato)

}
