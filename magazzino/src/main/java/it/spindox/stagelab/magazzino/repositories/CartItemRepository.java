package it.spindox.stagelab.magazzino.repositories;

import it.spindox.stagelab.magazzino.entities.CartItem;
import it.spindox.stagelab.magazzino.entities.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCartIdAndStatus(String cartId, ReservationStatus status);

    Optional<CartItem> findByCartIdAndProdottoIdAndStatus(
            String cartId,
            Long prodottoId,
            ReservationStatus status
    );

    @Query("""
        SELECT c FROM CartItem c
        WHERE c.status = :status
        AND c.expiresAt < :expiresAt
    """)
    List<CartItem> findExpiredItems(
            @Param("status") ReservationStatus status,
            @Param("expiresAt") LocalDateTime expiresAt
    );

    @Query("""
        SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END
        FROM CartItem c
        WHERE c.cartId = :cartId
        AND c.status = 'RESERVED'
        AND c.expiresAt > :now
    """)
    boolean hasActiveCart(
            @Param("cartId") String cartId,
            @Param("now") LocalDateTime now
    );
}