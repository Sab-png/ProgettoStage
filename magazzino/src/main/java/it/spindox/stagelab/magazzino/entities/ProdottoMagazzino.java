package it.spindox.stagelab.magazzino.entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Data
@Entity
@Table(name = "PRODOTTO_MAGAZZINO")
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

    @ManyToOne
    @JoinColumn(name = "ID_PRODOTTO", nullable = false)
    private Prodotto prodotto;

    @ManyToOne
    @JoinColumn(name = "ID_MAGAZZINO", nullable = false)
    private Magazzino magazzino;

    @Column(name = "QUANTITA", nullable = false)
    private Integer quantita;

    /**
     * Stato derivato
     */

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 5, nullable = false)
    private StockStatusProdotto status;

    /**
     *  SCORTA MIN
     */
    @Column(name = "SCORTA_MIN")
    @Min(0)
    private Integer scortaMin;

    /**
     *  Logica di dominio centrale
     * - garantisce SCORTA_MIN
     * - ricalcola sempre STATUS
     */
    @PrePersist
    @PreUpdate
    private void applyStockLogic() {

        //  Default SCORTA_MIN
        if (this.scortaMin == null) {
            this.scortaMin = DEFAULT_SCORTA_MIN;
            log.debug("SCORTA_MIN NULL → default {}", DEFAULT_SCORTA_MIN);
        }

        //  Calcolo STATUS tramite enum
        this.status = StockStatusProdotto.fromQuantita(this.quantita, this.scortaMin);
    }

    /**
     *  Metodo di dominio
     * (opzionale, ma utile)
     */
    @Transient
    public Integer getSogliaMinima() {
        return scortaMin;
    }
}