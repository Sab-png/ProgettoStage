package it.spindox.stagelab.magazzino.entities;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "PRODOTTO_MAGAZZINO")
public class ProdottoMagazzino {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prod_mag_seq_gen")
    @SequenceGenerator(
            name = "prod_mag_seq_gen",
            sequenceName = "PRODOTTO_MAGAZZINO_SEQ",
            allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_PRODOTTO", nullable = false)
    private Prodotto prodotto;

    @ManyToOne
    @JoinColumn(name = "ID_MAGAZZINO", nullable = false)
    private Magazzino magazzino;

    @Column(name = "QUANTITA")
    private Integer quantita;

    public int getSogliaMinima() {
        return 0;
    }

    public void setStatus(StockStatus nuovoStatus) {
    }

    public StockStatus getStatus() {
        return null;
    }
}

