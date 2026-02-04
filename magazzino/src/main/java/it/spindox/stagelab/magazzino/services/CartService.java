package it.spindox.stagelab.magazzino.services;

import it.spindox.stagelab.magazzino.dto.request.AddToCartRequest;
import it.spindox.stagelab.magazzino.dto.request.CheckoutRequest;
import it.spindox.stagelab.magazzino.dto.request.UpdateCartItemRequest;
import it.spindox.stagelab.magazzino.dto.response.CartItemResponse;
import it.spindox.stagelab.magazzino.dto.response.CartResponse;
import it.spindox.stagelab.magazzino.dto.response.CheckoutResponse;

public interface CartService {

    CartItemResponse addToCart(String sessionId, AddToCartRequest request);

    CartResponse getCart(String sessionId);

    CartItemResponse updateCartItem(String sessionId, Long cartItemId, UpdateCartItemRequest request);

    void removeFromCart(String sessionId, Long cartItemId);

    CheckoutResponse checkout(String sessionId, CheckoutRequest request);

    void cleanExpiredReservations();
}