package it.spindox.stagelab.magazzino.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "MAGAZZINO")
public class Magazzino {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "_seq_magazzino_gen"
    )
    @SequenceGenerator(
            name = "magazzino_seq_gen",
            sequenceName = "MAGAZZINO_SEQ",
            allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;
    @Column(name = "NOME")
    private String nome;
    @Column(name = "INDIRIZZO")
    private String indirizzo;
    @Column(name = "CAPACITA")
    private Integer capacita;
    @OneToMany(mappedBy = "magazzino")
    private List<ProdottoMagazzino> prodottoMagazzino;

}

