package it.spindox.stagelab.magazzino.services;

import it.spindox.stagelab.magazzino.dto.request.AddToCartRequest;
import it.spindox.stagelab.magazzino.dto.request.CheckoutRequest;
import it.spindox.stagelab.magazzino.dto.request.UpdateCartItemRequest;
import it.spindox.stagelab.magazzino.dto.response.CartItemResponse;
import it.spindox.stagelab.magazzino.dto.response.CartResponse;
import it.spindox.stagelab.magazzino.dto.response.CheckoutResponse;
import it.spindox.stagelab.magazzino.entities.Cart;
import it.spindox.stagelab.magazzino.entities.CartItem;
import it.spindox.stagelab.magazzino.entities.Prodotto;
import it.spindox.stagelab.magazzino.entities.ReservationStatus;
import it.spindox.stagelab.magazzino.exceptions.*;
import it.spindox.stagelab.magazzino.mappers.CartMapper;
import it.spindox.stagelab.magazzino.repositories.CartItemRepository;
import it.spindox.stagelab.magazzino.repositories.CartRepository;
import it.spindox.stagelab.magazzino.repositories.ProdottoRepository;
import it.spindox.stagelab.magazzino.repositories.ProdottoMagazzinoRepository;
import it.spindox.stagelab.magazzino.repositories.MagazzinoRepository;
import jakarta.persistence.EntityNotFoundException;
//import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProdottoRepository prodottoRepository;
    private final ProdottoMagazzinoRepository prodottoMagazzinoRepository;
    private final MagazzinoRepository magazzinoRepository;

    @Value("${cart.reservation.minutes:20}")
    private Integer reservationMinutes;

    public CartServiceImpl(CartItemRepository cartItemRepository,
                           CartRepository cartRepository,
                           ProdottoRepository prodottoRepository,
                           ProdottoMagazzinoRepository prodottoMagazzinoRepository,
                           MagazzinoRepository magazzinoRepository) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.prodottoRepository = prodottoRepository;
        this.prodottoMagazzinoRepository = prodottoMagazzinoRepository;
        this.magazzinoRepository = magazzinoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse createCart(String cartId, Long magazzinoId) {
        boolean exists = cartRepository.existsById(cartId);
        if (exists) {
            throw new IllegalStateException("Esiste già un carrello per l'ID: " + cartId);
        }

        // Creazione carrello "vuoto" persistita
        var magazzino = magazzinoRepository.findById(magazzinoId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Magazzino con ID " + magazzinoId + " non trovato"
                ));

        var cart = new Cart();
        cart.setCartId(cartId);
        cart.setMagazzino(magazzino);
        cart.setCreatedAt(LocalDateTime.now());
        Cart saved = cartRepository.save(cart);

        return CartMapper.toCartResponse(saved, List.of());
    }

    @Override
    @Transactional
    public CartItemResponse addToCart(String cartId, AddToCartRequest request) {
        //log.info("Aggiunta prodotto {} al carrello {}", request.getProdottoId(), cartId);

        // Pulisce prenotazioni scadute
        cleanExpiredReservations();

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Carrello con ID " + cartId + " non trovato"
                ));

        // Recupera il prodotto con lock pessimistico per evitare race condition
        Prodotto prodotto = prodottoRepository.findByIdWithLock(request.getProdottoId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Prodotto con ID " + request.getProdottoId() + " non trovato"
                ));

        // Recupera il magazzino del carrello e verifica coerenza con richiesta
        var magazzino = cart.getMagazzino();
        if (!magazzino.getId().equals(request.getMagazzinoId())) {
            throw new IllegalStateException("Il magazzino dell'item non coincide con il magazzino del carrello");
        }

        // Verifica esistenza relazione prodotto-magazzino e quantità
        var prodottoMagazzino = prodottoMagazzinoRepository
                .findByProdottoIdAndMagazzinoId(request.getProdottoId(), request.getMagazzinoId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Prodotto %d non presente nel magazzino %d",
                                request.getProdottoId(), request.getMagazzinoId())
                ));

        // Verifica disponibilità stock globale
        if (prodotto.getAvailableStock() < request.getQuantity()) {
            throw new InsufficientStockException(
                    String.format("Stock insufficiente per '%s'. Disponibili: %d, Richiesti: %d",
                            prodotto.getNome(),
                            prodotto.getAvailableStock(),
                            request.getQuantity())
            );
        }

        // Verifica disponibilità stock nel magazzino specifico
        if (prodottoMagazzino.getQuantita() < request.getQuantity()) {
            throw new InsufficientStockException(
                    String.format("Stock insufficiente nel magazzino selezionato. " +
                                    "Quantità magazzino: %d, Richiesti: %d",
                            prodottoMagazzino.getQuantita(),
                            request.getQuantity())
            );
        }

        // Verifica se l'utente ha già questo prodotto nel carrello
        Optional<CartItem> existingItem = cartItemRepository
                .findByCartIdAndProdottoIdAndStatus(
                        cartId,
                        request.getProdottoId(),
                        ReservationStatus.RESERVED
                );

        CartItem cartItem;

        if (existingItem.isPresent()) {
            // Aggiorna quantità esistente
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

            //log.info("Aggiornato carrello esistente {} con nuova quantità {}", cartItem.getId(), newQuantity);
        } else {
            // Crea nuova prenotazione
            cartItem = new CartItem();
            cartItem.setCartId(cartId);
            cartItem.setProdotto(prodotto);
            cartItem.setMagazzino(magazzino);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setReservedAt(LocalDateTime.now());
            cartItem.setExpiresAt(LocalDateTime.now().plusMinutes(reservationMinutes));
            cartItem.setStatus(ReservationStatus.RESERVED);

            prodotto.setAvailableStock(prodotto.getAvailableStock() - request.getQuantity());

            //log.info("Creato nuovo elemento carrello per prodotto {} con quantità {}",
                    //prodotto.getId(), request.getQuantity(););
        }

        prodottoRepository.save(prodotto);
        CartItem saved = cartItemRepository.save(cartItem);

        return CartMapper.toItemResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart(String cartId) {
        //log.info("Recupero carrello {}", cartId);

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Carrello con ID " + cartId + " non trovato"
                ));

        List<CartItem> items = cartItemRepository.findByCartId(cartId);

        return CartMapper.toCartResponse(cart, items);
    }

    @Override
    @Transactional
    public CartItemResponse updateCartItem(String cartId, Long cartItemId,
                                           UpdateCartItemRequest request) {
        //log.info("Aggiornamento elemento carrello {} per carrello {}", cartItemId, cartId);

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException(
                        "Elemento carrello con ID " + cartItemId + " non trovato"
                ));

        // Verifica che l'item appartenga al carrello
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
            // Aumento quantità - verifica disponibilità
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
            // Riduzione quantità - rilascia stock
            prodotto.setAvailableStock(prodotto.getAvailableStock() + Math.abs(difference));
        }

        cartItem.setQuantity(request.getQuantity());
        cartItem.setExpiresAt(LocalDateTime.now().plusMinutes(reservationMinutes));

        prodottoRepository.save(prodotto);
        CartItem saved = cartItemRepository.save(cartItem);

        //log.info("Elemento carrello {} aggiornato a quantità {}", cartItemId, request.getQuantity());

        return CartMapper.toItemResponse(saved);
    }

    @Override
    @Transactional
    public CartResponse removeFromCart(String cartId, Long cartItemId) {
        //log.info("Rimozione elemento carrello {} per carrello {}", cartItemId, cartId);

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
            // Rilascia lo stock
            Prodotto prodotto = cartItem.getProdotto();
            prodotto.setAvailableStock(prodotto.getAvailableStock() + cartItem.getQuantity());
            prodottoRepository.save(prodotto);

            //log.info("Rilasciate {} unità del prodotto {} nello stock",
                    //cartItem.getQuantity(), prodotto.getId(););
        }

        cartItemRepository.delete(cartItem);

        // Ritorna il carrello aggiornato
        List<CartItem> items = cartItemRepository
                .findByCartId(cartId);

        return CartMapper.toCartResponse(items);
    }

    @Override
    @Transactional
    public CheckoutResponse checkout(String cartId, CheckoutRequest request) {
        //log.info("Elaborazione checkout per carrello {}", cartId);

        List<CartItem> items = cartItemRepository
                .findByCartIdAndStatus(cartId, ReservationStatus.RESERVED);

        if (items.isEmpty()) {
            throw new EmptyCartException("Il carrello è vuoto");
        }

        // Verifica che le prenotazioni non siano scadute
        LocalDateTime now = LocalDateTime.now();
        boolean hasExpired = items.stream()
                .anyMatch(item -> item.getExpiresAt().isBefore(now));

        if (hasExpired) {
            cleanExpiredReservations();
            throw new ReservationExpiredException(
                    "La sessione è scaduta. I prodotti sono stati rilasciati."
            );
        }

        // Calcola totale
        double total = items.stream()
                .mapToDouble(item -> item.getProdotto().getPrezzo() * item.getQuantity())
                .sum();

        // Aggiorna lo stock totale e rimuove le prenotazioni completate
        for (CartItem item : items) {
            Prodotto prodotto = item.getProdotto();

            // Lo stock disponibile è già stato decrementato, ora decrementa anche quello totale
            prodotto.setTotalStock(prodotto.getTotalStock() - item.getQuantity());
            prodottoRepository.save(prodotto);

            // Rimuove definitivamente la riga di carrello
            cartItemRepository.delete(item);

            //log.info("Completato acquisto di {} unità del prodotto {}",
                    //item.getQuantity(), prodotto.getId());
        }

        // Genera ID ordine (qui dovresti creare l'entità Order se esiste)
        String orderId = "ORD-" + System.currentTimeMillis();

        //log.info("Checkout completato con ID ordine {}", orderId);

        CheckoutResponse response = new CheckoutResponse();
        response.setOrderId(orderId);
        response.setMessage("Ordine completato con successo");
        response.setTotalAmount(total);

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

        //log.info("Trovati {} elementi carrello scaduti da pulire", expiredItems.size());

        for (CartItem item : expiredItems) {
            // Rilascia lo stock
            Prodotto prodotto = item.getProdotto();
            prodotto.setAvailableStock(prodotto.getAvailableStock() + item.getQuantity());
            prodottoRepository.save(prodotto);

            // Marca come scaduto ma non elimina il record
            item.setStatus(ReservationStatus.EXPIRED);
            cartItemRepository.save(item);
        }
    }
}