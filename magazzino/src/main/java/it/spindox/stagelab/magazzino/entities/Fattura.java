
package it.spindox.stagelab.magazzino.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    @Positive(message = "L'importo deve essere maggiore di zero")
    @Column(name = "IMPORTO", nullable = false, precision = 15, scale = 2)
    private BigDecimal importo;

    @Positive(message = "La quantità deve essere maggiore di zero")
    @Column(name = "QUANTITA", nullable = false)
    private Integer quantita;

    @ManyToOne
    @JoinColumn(name = "ID_PRODOTTO", nullable = false)
    private Prodotto prodotto;
public void setIdProdotto(Long idProdotto) {}public void setData(LocalDate data) {}}
