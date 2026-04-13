package it.spindox.stagelab.magazzino.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 20, nullable = false)
    private ReservationStatus status = ReservationStatus.RESERVED;

    @Column(name = "SHIPPING_ADDRESS", length = 500)
    private String shippingAddress;

    @Column(name = "SHIPPING_EMAIL", length = 255)
    private String shippingEmail;

    /** Fascia oraria di consegna: "09:00-12:00", "12:00-15:00", "15:00-18:00", "18:00-21:00" */
    @Column(name = "DELIVERY_SLOT", length = 20)
    private String deliveryTimeSlot;

    @Column(name = "DELIVERY_DATE")
    private LocalDate deliveryDate;

    @Column(name = "CHECKED_OUT_AT")
    private LocalDateTime checkedOutAt;
}