package it.spindox.stagelab.magazzino.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "PRODOTTO_MAGAZZINO")
public class ProdottoMagazzino {

    @Id
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Prodotto getProdotto() {
        return prodotto;
    }

    public void setProdotto(Prodotto prodotto) {
        this.prodotto = prodotto;
    }

    public Magazzino getMagazzino() {
        return magazzino;
    }

    public void setMagazzino(Magazzino magazzino) {
        this.magazzino = magazzino;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }
}

