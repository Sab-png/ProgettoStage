package it.spindox.stagelab.magazzino.services;

import it.spindox.stagelab.magazzino.dto.request.AddToCartRequest;
import it.spindox.stagelab.magazzino.dto.request.CheckoutRequest;
import it.spindox.stagelab.magazzino.dto.request.UpdateCartItemRequest;
import it.spindox.stagelab.magazzino.dto.response.CartItemResponse;
import it.spindox.stagelab.magazzino.dto.response.CartResponse;
import it.spindox.stagelab.magazzino.dto.response.CheckoutResponse;
import it.spindox.stagelab.magazzino.dto.response.PlaceholderUserResponse;
import it.spindox.stagelab.magazzino.entities.*;
import it.spindox.stagelab.magazzino.exceptions.*;
import it.spindox.stagelab.magazzino.mappers.CartMapper;
import it.spindox.stagelab.magazzino.repositories.CartItemRepository;
import it.spindox.stagelab.magazzino.repositories.CartRepository;
import it.spindox.stagelab.magazzino.repositories.ProdottoRepository;
import it.spindox.stagelab.magazzino.repositories.ProdottoMagazzinoRepository;
import it.spindox.stagelab.magazzino.repositories.MagazzinoRepository;
import it.spindox.stagelab.magazzino.clients.UserClient;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProdottoRepository prodottoRepository;
    private final ProdottoMagazzinoRepository prodottoMagazzinoRepository;
    private final MagazzinoRepository magazzinoRepository;
    private final UserClient userClient;

    @Value("${cart.reservation.minutes:20}")
    private Integer reservationMinutes;

    public CartServiceImpl(CartItemRepository cartItemRepository,
                           CartRepository cartRepository,
                           ProdottoRepository prodottoRepository,
                           ProdottoMagazzinoRepository prodottoMagazzinoRepository,
                           MagazzinoRepository magazzinoRepository,
                           UserClient userClient) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.prodottoRepository = prodottoRepository;
        this.prodottoMagazzinoRepository = prodottoMagazzinoRepository;
        this.magazzinoRepository = magazzinoRepository;
        this.userClient = userClient;
    }

    @Override
    @Transactional
    public CartResponse createCart(Long magazzinoId, String email) {
        String cartId = UUID.randomUUID().toString();

        Magazzino magazzino = magazzinoRepository.findById(magazzinoId)
                .orElseThrow(() -> new MagazzinoNotFoundException(
                        "Magazzino con ID " + magazzinoId + " non trovato"
                ));

        Cart cart = new Cart();
        cart.setCartId(cartId);
        cart.setMagazzino(magazzino);
        cart.setCreatedAt(LocalDateTime.now());
        cart.setStatus(ReservationStatus.RESERVED);
        cart.setShippingEmail(email); // salvata subito per poterla usare nella GET
        cartRepository.save(cart);

        return CartMapper.toCartResponse(cart, List.of(), null);
    }

    @Override
    @Transactional
    public CartItemResponse addToCart(String cartId, AddToCartRequest request) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Carrello con ID " + cartId + " non trovato"
                ));

        Prodotto prodotto = prodottoRepository.findByIdWithLock(request.getProdottoId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Prodotto con ID " + request.getProdottoId() + " non trovato"
                ));

        var magazzino = cart.getMagazzino();
        if (!magazzino.getId().equals(request.getMagazzinoId())) {
            throw new IllegalStateException("Il magazzino dell'item non coincide con il magazzino del carrello");
        }

        var prodottoMagazzino = prodottoMagazzinoRepository
                .findByProdottoIdAndMagazzinoId(request.getProdottoId(), request.getMagazzinoId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Prodotto %d non presente nel magazzino %d",
                                request.getProdottoId(), request.getMagazzinoId())
                ));

        if (prodotto.getAvailableStock() < request.getQuantity()) {
            throw new InsufficientStockException(
                    String.format("Stock insufficiente per '%s'. Disponibili: %d, Richiesti: %d",
                            prodotto.getNome(),
                            prodotto.getAvailableStock(),
                            request.getQuantity())
            );
        }

        if (prodottoMagazzino.getQuantita() < request.getQuantity()) {
            throw new InsufficientStockException(
                    String.format("Stock insufficiente nel magazzino selezionato. " +
                                    "Quantità magazzino: %d, Richiesti: %d",
                            prodottoMagazzino.getQuantita(),
                            request.getQuantity())
            );
        }

        Optional<CartItem> existingItem = cartItemRepository
                .findByCartIdAndProdottoIdAndStatus(
                        cartId,
                        request.getProdottoId(),
                        ReservationStatus.RESERVED
                );

        CartItem cartItem;

        if (existingItem.isPresent()) {
            cartItem = existingItem.get();
            int newQuantity = cartItem.getQuantity() + request.getQuantity();

            if (prodotto.getAvailableStock() < request.getQuantity()) {
                throw new InsufficientStockException(
                        "Stock insufficiente per aggiungere altri articoli"
                );
            }

            if (!cartItem.getMagazzino().getId().equals(magazzino.getId())) {
                throw new IllegalStateException(
                        "Tutti gli articoli del carrello devono appartenere allo stesso magazzino"
                );
            }

            cartItem.setQuantity(newQuantity);
            cartItem.setExpiresAt(LocalDateTime.now().plusMinutes(reservationMinutes));
            prodotto.setAvailableStock(prodotto.getAvailableStock() - request.getQuantity());
        } else {
            cartItem = new CartItem();
            cartItem.setCartId(cartId);
            cartItem.setProdotto(prodotto);
            cartItem.setMagazzino(magazzino);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setReservedAt(LocalDateTime.now());
            cartItem.setExpiresAt(LocalDateTime.now().plusMinutes(reservationMinutes));
            cartItem.setStatus(ReservationStatus.RESERVED);

            prodotto.setAvailableStock(prodotto.getAvailableStock() - request.getQuantity());
        }

        prodottoRepository.save(prodotto);
        CartItem saved = cartItemRepository.save(cartItem);

        return CartMapper.toItemResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart(String cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Carrello con ID " + cartId + " non trovato"
                ));

        List<CartItem> items = cartItemRepository.findByCartId(cartId);

        // Recupera i dati utente dal placeholder se l'email è presente sul carrello
        PlaceholderUserResponse user = null;
        if (cart.getShippingEmail() != null) {
            user = userClient.findByEmail(cart.getShippingEmail()).orElse(null);
        }

        return CartMapper.toCartResponse(cart, items, user);
    }

    @Override
    @Transactional
    public CartItemResponse updateCartItem(String cartId, Long cartItemId,
                                           UpdateCartItemRequest request) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(
                        "Elemento carrello con ID " + cartItemId + " non trovato"
                ));

        if (!cartItem.getCartId().equals(cartId)) {
            throw new UnauthorizedAccessException(
                    "Non autorizzato ad accedere a questo elemento"
            );
        }

        if (cartItem.getStatus() != ReservationStatus.RESERVED) {
            throw new IllegalStateException(
                    "Impossibile modificare un elemento scaduto o completato"
            );
        }

        Prodotto prodotto = prodottoRepository.findByIdWithLock(cartItem.getProdotto().getId())
                .orElseThrow(() -> new EntityNotFoundException("Prodotto non trovato"));

        var prodottoMagazzino = prodottoMagazzinoRepository
                .findByProdottoIdAndMagazzinoId(
                        cartItem.getProdotto().getId(),
                        cartItem.getMagazzino().getId()
                )
                .orElseThrow(() -> new EntityNotFoundException(
                        "Relazione prodotto-magazzino non trovata"
                ));

        int difference = request.getQuantity() - cartItem.getQuantity();

        if (difference > 0) {
            if (prodotto.getAvailableStock() < difference) {
                throw new InsufficientStockException(
                        String.format("Stock insufficiente. Disponibili: %d",
                                prodotto.getAvailableStock())
                );
            }

            if (prodottoMagazzino.getQuantita() < request.getQuantity()) {
                throw new InsufficientStockException(
                        String.format("Stock insufficiente nel magazzino selezionato. " +
                                        "Quantità magazzino: %d, Richiesti: %d",
                                prodottoMagazzino.getQuantita(),
                                request.getQuantity())
                );
            }
            prodotto.setAvailableStock(prodotto.getAvailableStock() - difference);
        } else if (difference < 0) {
            prodotto.setAvailableStock(prodotto.getAvailableStock() + Math.abs(difference));
        }

        cartItem.setQuantity(request.getQuantity());
        cartItem.setExpiresAt(LocalDateTime.now().plusMinutes(reservationMinutes));

        prodottoRepository.save(prodotto);
        CartItem saved = cartItemRepository.save(cartItem);

        return CartMapper.toItemResponse(saved);
    }

    @Override
    @Transactional
    public CartResponse removeFromCart(String cartId, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(
                        "Elemento carrello con ID " + cartItemId + " non trovato"
                ));

        if (!cartItem.getCartId().equals(cartId)) {
            throw new UnauthorizedAccessException(
                    "Non autorizzato ad accedere a questo elemento"
            );
        }

        if (cartItem.getStatus() == ReservationStatus.RESERVED) {
            Prodotto prodotto = cartItem.getProdotto();
            prodotto.setAvailableStock(prodotto.getAvailableStock() + cartItem.getQuantity());
            prodottoRepository.save(prodotto);
        }

        cartItemRepository.delete(cartItem);

        List<CartItem> items = cartItemRepository.findByCartId(cartId);

        return CartMapper.toCartResponse(items);
    }

    @Override
    @Transactional
    public CheckoutResponse checkout(String cartId, CheckoutRequest request) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Carrello con ID " + cartId + " non trovato"
                ));

        if (cart.getStatus() != ReservationStatus.RESERVED) {
            throw new IllegalStateException(
                    "Il carrello è già " + cart.getStatus().name().toLowerCase()
            );
        }

        List<CartItem> items = cartItemRepository
                .findByCartIdAndStatus(cartId, ReservationStatus.RESERVED);

        if (items.isEmpty()) {
            throw new EmptyCartException("Il carrello è vuoto");
        }

        LocalDateTime now = LocalDateTime.now();
        boolean hasExpired = items.stream()
                .anyMatch(item -> item.getExpiresAt().isBefore(now));

        if (hasExpired) {
            cleanExpiredReservations();
            throw new ReservationExpiredException(
                    "La sessione è scaduta. I prodotti sono stati rilasciati."
            );
        }

        double total = items.stream()
                .mapToDouble(item -> item.getProdotto().getPrezzo() * item.getQuantity())
                .sum();

        for (CartItem item : items) {
            Prodotto prodotto = item.getProdotto();

            prodotto.setTotalStock(prodotto.getTotalStock() - item.getQuantity());
            prodotto.setAvailableStock(prodotto.getTotalStock());
            prodottoRepository.save(prodotto);

            var prodottoMagazzino = prodottoMagazzinoRepository
                    .findByProdottoIdAndMagazzinoId(
                            prodotto.getId(),
                            item.getMagazzino().getId()
                    )
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Relazione prodotto-magazzino non trovata per prodotto "
                                    + prodotto.getId()
                    ));
            prodottoMagazzino.setQuantita(prodottoMagazzino.getQuantita() - item.getQuantity());
            prodottoMagazzinoRepository.save(prodottoMagazzino);

            item.setStatus(ReservationStatus.COMPLETED);
            cartItemRepository.save(item);
        }

        cart.setStatus(ReservationStatus.COMPLETED);
        cart.setShippingAddress(request.getShippingAddress());
        cart.setShippingEmail(request.getShippingEmail());
        cart.setDeliveryTimeSlot(request.getDeliveryTimeSlot());
        cart.setDeliveryDate(request.getDeliveryDate());
        cart.setCheckedOutAt(now);
        cartRepository.save(cart);

        String orderId = "ORD-" + System.currentTimeMillis();

        CheckoutResponse response = new CheckoutResponse();
        response.setOrderId(orderId);
        response.setMessage("Ordine completato con successo");
        response.setTotalAmount(total);

        // Cerca utente sul placeholder per email di spedizione
        userClient.findByEmail(request.getShippingEmail())
                .ifPresent(response::setUser);

        return response;
    }

    @Override
    @Transactional
    public void cleanExpiredReservations() {
        LocalDateTime now = LocalDateTime.now();
        List<CartItem> expiredItems = cartItemRepository
                .findExpiredItems(ReservationStatus.RESERVED, now);

        if (expiredItems.isEmpty()) {
            return;
        }

        for (CartItem item : expiredItems) {
            Prodotto prodotto = item.getProdotto();
            prodotto.setAvailableStock(prodotto.getAvailableStock() + item.getQuantity());
            prodottoRepository.save(prodotto);

            item.setStatus(ReservationStatus.EXPIRED);
            cartItemRepository.save(item);
        }
    }
}