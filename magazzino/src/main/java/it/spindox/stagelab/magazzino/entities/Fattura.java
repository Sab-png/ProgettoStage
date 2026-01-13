package it.spindox.stagelab.magazzino.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "FATTURA")
public class Fattura {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "DATA_FATTURA", nullable = false)
    private LocalDate dataFattura;

    @Column(name = "IMPORTO", nullable = false)
    private BigDecimal importo;

    @ManyToOne
    @JoinColumn(name = "ID_PRODOTTO", nullable = false)
    private Prodotto prodotto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataFattura() {
        return dataFattura;
    }

    public void setDataFattura(LocalDate dataFattura) {
        this.dataFattura = dataFattura;
    }

    public BigDecimal getImporto() {
        return importo;
    }

    public void setImporto(BigDecimal importo) {
        this.importo = importo;
    }

    public Prodotto getProdotto() {
        return prodotto;
    }

    public void setProdotto(Prodotto prodotto) {
        this.prodotto = prodotto;
    }
}

