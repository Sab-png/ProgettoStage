package it.spindox.stagelab.magazzino.entities;
import it.spindox.stagelab.magazzino.converter.StockStatusConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.util.Objects;



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

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_PRODOTTO", nullable = false)
    private Prodotto prodotto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_MAGAZZINO", nullable = false)
    private Magazzino magazzino;

    @Column(name = "QUANTITA", nullable = false)
    private Integer quantita;
    @Convert(converter = StockStatusConverter.class) // usa il dbValue (GREEN/YELLOW/RED)
    @Column(name = "STATUS", length = 10, nullable = false)
    private StockStatusProdotto status;

    @Column(name = "SCORTA_MIN")
    @Min(0)
    private Integer scortaMin;

    @PrePersist
    @PreUpdate
    private void applyStockLogic() {

        // Default SCORTA_MIN se non fornita
        if (this.scortaMin == null) {
            this.scortaMin = DEFAULT_SCORTA_MIN;
            log.debug("SCORTA_MIN null → impostato default {}", DEFAULT_SCORTA_MIN);
        }

        // Lo status è sempre ricalcolato
        this.status = StockStatusProdotto.fromQuantita(
                Objects.requireNonNull(this.quantita),
                this.scortaMin
        );
    }
}