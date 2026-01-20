package it.spindox.stagelab.magazzino.entities;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "MAGAZZINO")
public class Magazzino {

    // ID generato tramite sequence Oracle
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "magazzino_seq_gen")
    @SequenceGenerator(name = "magazzino_seq_gen", sequenceName = "MAGAZZINO_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    // Nome del magazzino
    @Column(name = "NOME")
    private String nome;

    // Indirizzo del magazzino
    @Column(name = "INDIRIZZO")
    private String indirizzo;

    // Capacità massima del magazzino
    @Column(name = "CAPACITA")
    private Integer capacita;

    // Relazione 1:N con ProdottoMagazzino (un magazzino contiene più prodotti)
    @OneToMany(mappedBy = "magazzino")
    private List<ProdottoMagazzino> prodottoMagazzino;

    public <__TMP__> __TMP__ getDescrizione() {
        return null;
    }

    public void setDescrizione(String descrizione) {
    }
}


