package it.spindox.stagelab.magazzino.entities;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "PRODOTTO_MAGAZZINO")
public class ProdottoMagazzino {

    // ID generato tramite sequence Oracle
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prod_mag_seq_gen")
    @SequenceGenerator(name = "prod_mag_seq_gen", sequenceName = "PRODOTTO_MAGAZZINO_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    // Relazione molti-a-uno verso Prodotto
    @ManyToOne
    @JoinColumn(name = "ID_PRODOTTO", nullable = false)
    private Prodotto prodotto;

    // Relazione molti-a-uno verso Magazzino
    @ManyToOne
    @JoinColumn(name = "ID_MAGAZZINO", nullable = false)
    private Magazzino magazzino;

    // Quantità del prodotto presente in quel magazzino
    @Column(name = "QUANTITA")
    private Integer quantita;
}
