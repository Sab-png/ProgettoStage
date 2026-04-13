package it.spindox.stagelab.magazzino.mappers;

import it.spindox.stagelab.magazzino.dto.response.CartItemResponse;
import it.spindox.stagelab.magazzino.dto.response.CartResponse;
import it.spindox.stagelab.magazzino.dto.response.PlaceholderUserResponse;
import it.spindox.stagelab.magazzino.entities.Cart;
import it.spindox.stagelab.magazzino.entities.CartItem;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class CartMapper {

    public static CartItemResponse toItemResponse(CartItem item) {
        CartItemResponse response = new CartItemResponse();
        response.setId(item.getId());
        response.setProdottoId(item.getProdotto().getId());
        response.setProdottoNome(item.getProdotto().getNome());
        // response.setProdottoImmagine(item.getProdotto().getImmagine());
        response.setPrezzoProdotto(item.getProdotto().getPrezzo());
        if (item.getMagazzino() != null) {
            response.setMagazzinoId(item.getMagazzino().getId());
        }
        response.setQuantity(item.getQuantity());
        response.setSubtotale(item.getProdotto().getPrezzo() * item.getQuantity());
        response.setReservedAt(item.getReservedAt());
        response.setExpiresAt(item.getExpiresAt());
        response.setStatus(item.getStatus().name());

        long seconds = Duration.between(LocalDateTime.now(), item.getExpiresAt()).getSeconds();
        response.setSecondsRemaining(Math.max(0, seconds));

        return response;
    }

    // Overload base: solo items (usato internamente e da removeFromCart)
    public static CartResponse toCartResponse(List<CartItem> items) {
        CartResponse response = new CartResponse();

        List<CartItemResponse> itemResponses = items.stream()
                .map(CartMapper::toItemResponse)
                .toList();

        response.setItems(itemResponses);
        response.setTotalItems(
                items.stream().mapToInt(CartItem::getQuantity).sum()
        );

        double total = items.stream()
                .mapToDouble(item -> item.getProdotto().getPrezzo() * item.getQuantity())
                .sum();
        response.setTotalPrice(total);

        Long minSeconds = itemResponses.stream()
                .map(CartItemResponse::getSecondsRemaining)
                .min(Long::compareTo)
                .orElse(0L);

        response.setSecondsRemaining(minSeconds);
        response.setCartActive(!items.isEmpty() && minSeconds > 0);

        return response;
    }

    // Overload con Cart e user: usato da getCart e createCart
    public static CartResponse toCartResponse(Cart cart, List<CartItem> items, PlaceholderUserResponse user) {
        CartResponse response = toCartResponse(items);
        if (cart != null && cart.getMagazzino() != null) {
            response.setMagazzinoId(cart.getMagazzino().getId());
        }
        response.setUser(user);
        return response;
    }

    // Overload con Cart senza user: usato da removeFromCart
    public static CartResponse toCartResponse(Cart cart, List<CartItem> items) {
        return toCartResponse(cart, items, null);
    }
}