package it.spindox.stagelab.magazzino.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "PRODOTTO_MAGAZZINO")
@SequenceGenerator(
        name = "prodotto_magazzino_seq_gen",
        sequenceName = "PRODOTTO_MAGAZZINO_SEQ",
        allocationSize = 1
)

public class ProdottoMagazzino {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "prodotto_magazzino_seq_gen"
    )
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_PRODOTTO", nullable = false)
    private Prodotto prodotto;

    @ManyToOne
    @JoinColumn(name = "ID_MAGAZZINO", nullable = false)
    private Magazzino magazzino;

    @Column(name = "QUANTITA", nullable = false)
    private Integer quantita;
}


