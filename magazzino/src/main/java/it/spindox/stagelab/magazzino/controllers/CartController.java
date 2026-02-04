package it.spindox.stagelab.magazzino.controllers;

import it.spindox.stagelab.magazzino.dto.request.AddToCartRequest;
import it.spindox.stagelab.magazzino.dto.request.CheckoutRequest;
import it.spindox.stagelab.magazzino.dto.request.UpdateCartItemRequest;
import it.spindox.stagelab.magazzino.dto.response.CartItemResponse;
import it.spindox.stagelab.magazzino.dto.response.CartResponse;
import it.spindox.stagelab.magazzino.dto.response.CheckoutResponse;
import it.spindox.stagelab.magazzino.services.CartService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    // POST addToCart
    @PostMapping("/add")
    public ResponseEntity<CartItemResponse> addToCart(
            HttpSession session,
            @Valid @RequestBody AddToCartRequest request) {
        String sessionId = session.getId();
        return ResponseEntity.ok(service.addToCart(sessionId, request));
    }

    // GET getCart
    @GetMapping
    public ResponseEntity<CartResponse> getCart(HttpSession session) {
        String sessionId = session.getId();
        return ResponseEntity.ok(service.getCart(sessionId));
    }

    // PATCH updateCartItem
    @PatchMapping("/items/{itemId}")
    public ResponseEntity<CartItemResponse> updateCartItem(
            HttpSession session,
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        String sessionId = session.getId();
        return ResponseEntity.ok(service.updateCartItem(sessionId, itemId, request));
    }

    // DELETE removeFromCart
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeFromCart(
            HttpSession session,
            @PathVariable Long itemId) {
        String sessionId = session.getId();
        service.removeFromCart(sessionId, itemId);
        return ResponseEntity.noContent().build();
    }

    // POST checkout
    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> checkout(
            HttpSession session,
            @Valid @RequestBody CheckoutRequest request) {
        String sessionId = session.getId();
        return ResponseEntity.ok(service.checkout(sessionId, request));
    }
}