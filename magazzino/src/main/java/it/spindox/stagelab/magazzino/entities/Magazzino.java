package it.spindox.stagelab.magazzino.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "MAGAZZINO")
public class Magazzino {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOME", nullable = false)
    private String nome;

    @Column(name = "INDIRIZZO")
    private String indirizzo;

    @Column(name = "CAPACITA")
    private Integer capacita;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public Integer getCapacita() {
        return capacita;
    }

    public void setCapacita(Integer capacita) {
        this.capacita = capacita;
    }

}

