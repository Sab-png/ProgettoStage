package it.spindox.stagelab.magazzino.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "CART")
public class Cart {

    @Id
    @Column(name = "CART_ID", length = 255, nullable = false)
    private String cartId;

    @ManyToOne
    @JoinColumn(name = "ID_MAGAZZINO", referencedColumnName = "ID", nullable = false)
    private Magazzino magazzino;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;
}

