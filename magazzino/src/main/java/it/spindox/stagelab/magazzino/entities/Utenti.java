package it.spindox.stagelab.magazzino.entities;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "UTENTI")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Utenti {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "utenti_seq_gen"
    )
    @SequenceGenerator(
            name = "utenti_seq_gen",
            sequenceName = "UTENTI_SEQ",
            allocationSize = 1
    )

    private Long id;
    private String username;
    private String password;
    private String ruolo; // per role ( ADMIN O USER)

}