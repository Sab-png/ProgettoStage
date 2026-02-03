
package it.spindox.stagelab.magazzino.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@Data
@Entity
@Table(name = "FATTURA")
public class Fattura {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fattura_seq_gen")
    @SequenceGenerator(
            name = "fattura_seq_gen",
            sequenceName = "FATTURA_SEQ",
            allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    @Column(name = "NUMERO", nullable = false)
    private String numero;

    @Column(name = "DATA_FATTURA", nullable = false)
    private LocalDate dataFattura;

    @Positive
    @Column(name = "IMPORTO", nullable = false, precision = 15, scale = 2)
    private BigDecimal importo;

    @Positive
    @Column(name = "QUANTITA", nullable = false)
    private Integer quantita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PRODOTTO", nullable = false)
    private Prodotto prodotto;
}
