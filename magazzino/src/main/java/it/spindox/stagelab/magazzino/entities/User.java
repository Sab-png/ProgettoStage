package it.spindox.stagelab.magazzino.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "USER_ACCOUNT")
public class User {

    @Id
    @Column(name = "USERNAME", length = 50, nullable = false)
    private String username;

    @Column(name = "PASSWORD", length = 50, nullable = false)
    private String password;
}