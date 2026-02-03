package it.spindox.stagelab.magazzino.entities;
import jakarta.persistence.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Data
@Entity
@Table(name = "MAGAZZINO")
public class Magazzino {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "magazzino_seq_gen")
    @SequenceGenerator(name = "magazzino_seq_gen", sequenceName = "MAGAZZINO_SEQ", allocationSize = 1)
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
