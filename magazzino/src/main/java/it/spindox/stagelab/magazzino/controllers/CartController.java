package it.spindox.stagelab.magazzino.controllers;

import it.spindox.stagelab.magazzino.dto.request.AddToCartRequest;
import it.spindox.stagelab.magazzino.dto.request.CheckoutRequest;
import it.spindox.stagelab.magazzino.dto.request.UpdateCartItemRequest;
import it.spindox.stagelab.magazzino.dto.response.CartItemResponse;
import it.spindox.stagelab.magazzino.dto.response.CartResponse;
import it.spindox.stagelab.magazzino.dto.response.CheckoutResponse;
import it.spindox.stagelab.magazzino.services.CartService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    private static String resolveCartId(String headerCartId) {
        if (headerCartId == null || headerCartId.isBlank()) {
            return UUID.randomUUID().toString();
        }
        return headerCartId.trim();
    }

    // POST addToCart   --> complicazione inutile, semplifichiamo!!
    @PostMapping("/add")
    public ResponseEntity<CartItemResponse> addToCart(
            @RequestHeader(value = "X-Cart-Id", required = false) String cartId,
            @Valid @RequestBody AddToCartRequest request) {
        String resolvedCartId = resolveCartId(cartId);
        return ResponseEntity
                .ok()
                .header("X-Cart-Id", resolvedCartId)
                .body(service.addToCart(resolvedCartId, request));
    }

    // GET getCart
    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            @RequestHeader(value = "X-Cart-Id", required = false) String cartId) {
        String resolvedCartId = resolveCartId(cartId);
        return ResponseEntity
                .ok()
                .header("X-Cart-Id", resolvedCartId)
                .body(service.getCart(resolvedCartId));
    }

    // PATCH updateCartItem
    @PatchMapping("/items/{itemId}")
    public ResponseEntity<CartItemResponse> updateCartItem(
            @RequestHeader("X-Cart-Id") String cartId,
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        return ResponseEntity
                .ok()
                .header("X-Cart-Id", cartId)
                .body(service.updateCartItem(cartId, itemId, request));
    }

    // DELETE removeFromCart
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeFromCart(
            @RequestHeader("X-Cart-Id") String cartId,
            @PathVariable Long itemId) {
        service.removeFromCart(cartId, itemId);
        return ResponseEntity
                .noContent()
                .header("X-Cart-Id", cartId)
                .build();
    }

    // POST checkout
    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> checkout(
            @RequestHeader("X-Cart-Id") String cartId,
            @Valid @RequestBody CheckoutRequest request) {
        return ResponseEntity
                .ok()
                .header("X-Cart-Id", cartId)
                .body(service.checkout(cartId, request));
    }
}