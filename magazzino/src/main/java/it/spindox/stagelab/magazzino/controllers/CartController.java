package it.spindox.stagelab.magazzino.controllers;

import it.spindox.stagelab.magazzino.dto.request.AddToCartRequest;
import it.spindox.stagelab.magazzino.dto.request.CheckoutRequest;
import it.spindox.stagelab.magazzino.dto.request.UpdateCartItemRequest;
import it.spindox.stagelab.magazzino.dto.response.CartItemResponse;
import it.spindox.stagelab.magazzino.dto.response.CartResponse;
import it.spindox.stagelab.magazzino.dto.response.CheckoutResponse;
import it.spindox.stagelab.magazzino.services.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/magazzino/{magazzinoId}/cart")
public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    // POST createCart
    // email opzionale: se fornita viene salvata sul carrello e usata per recuperare i dati utente
    @PostMapping
    public ResponseEntity<CartResponse> createCart(
            @PathVariable Long magazzinoId,
            @RequestParam(value = "email", required = false) String email) {
        return ResponseEntity.ok(service.createCart(magazzinoId, email));
    }

    // POST addToCart
    @PostMapping("/add")
    public ResponseEntity<CartItemResponse> addToCart(
            @PathVariable Long magazzinoId,
            @RequestParam("cartId") String cartId,
            @Valid @RequestBody AddToCartRequest request) {
        request.setMagazzinoId(magazzinoId);
        return ResponseEntity.ok(service.addToCart(cartId, request));
    }

    // GET getCart
    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            @PathVariable Long magazzinoId,
            @RequestParam("cartId") String cartId) {
        return ResponseEntity.ok(service.getCart(cartId));
    }

    // PATCH updateCartItem
    @PatchMapping("/items/{itemId}")
    public ResponseEntity<CartItemResponse> updateCartItem(
            @PathVariable Long magazzinoId,
            @RequestParam("cartId") String cartId,
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        return ResponseEntity.ok(service.updateCartItem(cartId, itemId, request));
    }

    // DELETE removeFromCart
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CartResponse> removeFromCart(
            @PathVariable Long magazzinoId,
            @RequestParam("cartId") String cartId,
            @PathVariable Long itemId) {
        return ResponseEntity.ok(service.removeFromCart(cartId, itemId));
    }

    // POST checkout
    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> checkout(
            @PathVariable Long magazzinoId,
            @RequestParam("cartId") String cartId,
            @Valid @RequestBody CheckoutRequest request) {
        return ResponseEntity.ok(service.checkout(cartId, request));
    }
}