package it.spindox.stagelab.magazzino.entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
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

    public void setNumero(@NotBlank String numero) {
    }

    public void setData(@NotNull LocalDate data) {
    }

    public void setImporto(@NotNull @Positive BigDecimal importo) {
    }

    public void setIdProdotto(@NotNull Long idProdotto) {
    }

    public String getNumero() {
        return "";
    }

    public LocalDate getData() {
        return null;
    }
}


