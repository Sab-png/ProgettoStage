package it.spindox.stagelab.magazzino.jobs;

import it.spindox.stagelab.magazzino.services.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Job schedulato che si occupa di pulire periodicamente
 * le prenotazioni di carrello scadute e rilasciare lo stock.
 */
@Component
@Slf4j
public class CartCleanupJob {

    private final CartService cartService;

    public CartCleanupJob(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Esegue la pulizia delle prenotazioni scadute ogni 5 minuti.
     */
    @Scheduled(fixedRate = 300000) // ogni 5 minuti
    public void run() {
//        log.debug("Avvio job pulizia carrelli scaduti");
//        cartService.cleanExpiredReservations();
    }
}

