package it.spindox.stagelab.magazzino.entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;import java.util.List;

@Slf4j
@Data
@Entity
@Table(name = "PRODOTTO")
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prodotto_seq_gen")
    @SequenceGenerator(name = "prodotto_seq_gen", sequenceName = "PRODOTTO_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "DESCRIZIONE")
    private String descrizione;

    @Column(name = "PREZZO", precision = 10, scale = 2)
    private BigDecimal prezzo;

    /** 1 prodotto → molte fatture */
    @OneToMany(mappedBy = "prodotto")
    private List<Fattura> fatture;

    /** 1 prodotto → molti magazzini */
    @OneToMany(mappedBy = "prodotto")
    private List<ProdottoMagazzino> prodottoMagazzino;
public void setPrezzo(BigDecimal prezzo) {}
    public void setPrezzo(@Positive(message = "Il prezzo deve essere maggiore di zero") Double prezzo) {}}
