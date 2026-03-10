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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    //POST createCart
    @PostMapping
    public ResponseEntity<CartResponse> createCart(
            @RequestParam("magazzinoId") Long magazzinoId) {
        return ResponseEntity.ok(service.createCart(magazzinoId));
    }

    // POST addToCart
    @PostMapping("/add")
    public ResponseEntity<CartItemResponse> addToCart(
            @RequestParam("cartId") String cartId,
            @Valid @RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(service.addToCart(cartId, request));
    }

    // GET getCart
    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            @RequestParam("cartId") String cartId) {
        return ResponseEntity.ok(service.getCart(cartId));
    }

    // PATCH updateCartItem
    @PatchMapping("/items/{itemId}")
    public ResponseEntity<CartItemResponse> updateCartItem(
            @RequestParam("cartId") String cartId,
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        return ResponseEntity.ok(service.updateCartItem(cartId, itemId, request));
    }

    // DELETE removeFromCart
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CartResponse> removeFromCart(
            @RequestParam("cartId") String cartId,
            @PathVariable Long itemId) {
        CartResponse updatedCart = service.removeFromCart(cartId, itemId);
        return ResponseEntity.ok(updatedCart);
    }

    // POST checkout
    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> checkout(
            @RequestParam("cartId") String cartId,
            @Valid @RequestBody CheckoutRequest request) {
        return ResponseEntity.ok(service.checkout(cartId, request));
    }

}