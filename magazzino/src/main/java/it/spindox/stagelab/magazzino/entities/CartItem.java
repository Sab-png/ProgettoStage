package it.spindox.stagelab.magazzino.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "CART_ITEM")
@SequenceGenerator(
        name = "cart_item_seq_gen",
        sequenceName = "CART_ITEM_SEQ",
        allocationSize = 1
)
public class CartItem {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cart_item_seq_gen"
    )
    @Column(name = "ID")
    private Long id;

    @Column(name = "CART_ID", nullable = false, length = 255)
    private String cartId;

    @ManyToOne
    @JoinColumn(name = "ID_PRODOTTO", referencedColumnName = "ID")
    private Prodotto prodotto;

    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;

    @Column(name = "RESERVED_AT", nullable = false)
    private LocalDateTime reservedAt;

    @Column(name = "EXPIRES_AT", nullable = false)
    private LocalDateTime expiresAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private ReservationStatus status;
}