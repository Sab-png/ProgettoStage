package it.spindox.stagelab.magazzino.services;

import it.spindox.stagelab.magazzino.dto.request.AddToCartRequest;
import it.spindox.stagelab.magazzino.dto.request.CheckoutRequest;
import it.spindox.stagelab.magazzino.dto.request.UpdateCartItemRequest;
import it.spindox.stagelab.magazzino.dto.response.CartItemResponse;
import it.spindox.stagelab.magazzino.dto.response.CartResponse;
import it.spindox.stagelab.magazzino.dto.response.CheckoutResponse;

public interface CartService {

    CartItemResponse addToCart(String cartId, AddToCartRequest request);

    CartResponse getCart(String cartId);

    CartItemResponse updateCartItem(String cartId, Long cartItemId, UpdateCartItemRequest request);

    CartResponse removeFromCart(String cartId, Long cartItemId);

    CheckoutResponse checkout(String cartId, CheckoutRequest request);

    void cleanExpiredReservations();
}