package it.spindox.stagelab.magazzino.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;import java.util.List;



@Slf4j
@Entity
@Table(name = "PRODOTTO", schema = "MAGAZZINO")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prodotto_seq_gen")
    @SequenceGenerator(

            name = "prodotto_seq_gen",
            sequenceName = "SEQ_PRODOTTO",
            allocationSize = 1

    )
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "DESCRIZIONE")
    private String descrizione;

    @Column(name = "PREZZO", precision = 10, scale = 2)
    private BigDecimal prezzo;

    @OneToMany(mappedBy = "prodotto")
    private List<Fattura> fatture;

    @OneToMany(mappedBy = "prodotto")
    private List<ProdottoMagazzino> prodottoMagazzino;

    public void setPrezzo(BigDecimal prezzo) {
        log.info("Impostazione prezzo prodotto: {} -> {}", this.prezzo, prezzo);
        this.prezzo = prezzo;
    }
}
