package it.spindox.stagelab.magazzino.entities;
import jakarta.persistence.*;
import it.spindox.stagelab.magazzino.entities.StockStatus;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Entity
@Table(name = "PRODOTTO_MAGAZZINO")
public class ProdottoMagazzino {

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

    @ManyToOne
    @JoinColumn(name = "ID_PRODOTTO", nullable = false)
    private Prodotto prodotto;

    @ManyToOne
    @JoinColumn(name = "ID_MAGAZZINO", nullable = false)
    private Magazzino magazzino;

    @Column(name = "QUANTITA")
    private Integer quantita;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 20)
    private StockStatus status;

    @Column(name = "SCORTA_MIN")
    @Min(0)
    private Integer scortaMin;

    public Integer getSogliaMinima() {
        return 0;
    }
}
