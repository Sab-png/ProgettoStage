package it.spindox.stagelab.magazzino.entities;
import it.spindox.stagelab.magazzino.dto.fattura.FatturaResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "FATTURA")
public class Fattura extends FatturaResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fattura_seq_gen")
    @SequenceGenerator(
            name = "fattura_seq_gen",
            sequenceName = "FATTURA_SEQ",
            allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    @Column(name = "DATA_FATTURA")
    private LocalDate dataFattura;

    @Column(name = "IMPORTO")
    private Double importo;

    @OneToOne
    @JoinColumn(name = "ID_PRODOTTO", referencedColumnName = "ID")
    private Prodotto prodotto;

    public void setQuantita(@Positive(message = "La quantità deve essere maggiore di zero") Integer quantita) {
        
    }

    public void setImporto(@Positive(message = "L'importo deve essere maggiore di zero") BigDecimal importo) {
    }

    public void setNumero(String numero) {

    }
}
