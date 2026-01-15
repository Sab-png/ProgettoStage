package it.spindox.stagelab.magazzino.entities;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "PRODOTTO")
public class Prodotto {

    // ID generato tramite sequence Oracle
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prodotto_seq_gen")
    @SequenceGenerator(name = "prodotto_seq_gen", sequenceName = "PRODOTTO_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    // Nome del prodotto
    @Column(name = "NOME")
    private String nome;

    // Descrizione del prodotto
    @Column(name = "DESCRIZIONE")
    private String descrizione;

    // Prezzo del prodotto
    @Column(name = "PREZZO")
    private Double prezzo;

    // Relazione 1:1 con Fattura (una fattura per prodotto)
    @OneToOne(mappedBy = "prodotto")
    private Fattura fattura;

    // Relazione 1:N con ProdottoMagazzino (il prodotto può stare in più magazzini)
    @OneToMany(mappedBy = "prodotto")
    private List<ProdottoMagazzino> prodottoMagazzino;
}
