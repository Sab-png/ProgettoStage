package it.spindox.stagelab.magazzino.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "PRODOTTO")
public class Prodotto {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "prodotto_seq_gen"
    )
    @SequenceGenerator(
            name = "prodotto_seq_gen",
            sequenceName = "PRODOTTO_SEQ",
            allocationSize = 1
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
}
