package it.spindox.stagelab.magazzino.entities;
import jakarta.persistence.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;




@Slf4j
@Data
@Entity
@Table(name = "PRODOTTO_MAGAZZINO", schema = "MAGAZZINO")
public class ProdottoMagazzino {

    private static final int DEFAULT_SCORTA_MIN = 5;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "prod_mag_seq_gen"
    )
    @SequenceGenerator(
            name = "prod_mag_seq_gen",
            sequenceName = "PRODOTTO_MAGAZZINO_SEQ",
            allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_PRODOTTO", nullable = false)
    private Prodotto prodotto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_MAGAZZINO", nullable = false)
    private Magazzino magazzino;

    @Column(name = "QUANTITA", nullable = false)
    private Integer quantita;

    @Column(name = "SCORTA_MIN")
    private Integer scortaMin;

    @Transient
    public StockStatusProdotto getStatus() {
        return StockStatusProdotto.fromQuantita(
                this.quantita,
                this.scortaMin != null ? this.scortaMin : DEFAULT_SCORTA_MIN
        );
    }
}
