package it.spindox.stagelab.magazzino.entities;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "FATTURA")
public class Fattura {

    // ID generato tramite sequence Oracle
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fattura_seq_gen")
    @SequenceGenerator(name = "fattura_seq_gen", sequenceName = "FATTURA_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    // Data di emissione della fattura
    @Column(name = "DATA_FATTURA")
    private LocalDate dataFattura;

    // Importo totale della fattura
    @Column(name = "IMPORTO")
    private Double importo;

    // Relazione 1:1 con Prodotto (ogni fattura è legata ad un unico prodotto)
    @OneToOne
    @JoinColumn(name = "ID_PRODOTTO", referencedColumnName = "ID")
    private Prodotto prodotto;
}
