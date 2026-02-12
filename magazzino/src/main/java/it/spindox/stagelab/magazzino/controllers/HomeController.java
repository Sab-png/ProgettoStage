package it.spindox.stagelab.magazzino.controllers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.Instant;
import java.util.Map;

/**
 * Controller principale per gli endpoint pubblici dell'applicazione.
 *
 * Questa classe fornisce tre endpoint:
 * - "/"       : homepage semplice per verificare che l'app sia attiva
 * - "/health" : stato dell'applicazione (UP + timestamp)
 * - "/info"   : informazioni statiche sul servizio
 *
 * Viene utilizzato come entry point generale del microservizio.
 */
@RestController
public class HomeController {

    /**
     * Endpoint principale dell'applicazione.
     * Serve come "homepage" testuale del servizio.
     *
     * URL: GET /
     *
     * @return stringa indicante che l'API è avviata correttamente
     */
    @GetMapping("/home")
    public String home() {
        return "Magazzino API Running ☻";
    }

    /**
     * Endpoint di health-check
     * - monitoraggio e logging
     * URL: GET /health
     *
     * @return mappa con stato e timestamp corrente
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
                "status", "UP",
                "timestamp", Instant.now().toString()
        );
    }

    /**
     * Endpoint informativo sull'applicazione.
     *
     * Questo endpoint è utile per:
     * - debug
     * - logging
     * - strumenti esterni che interrogano i microservizi
     * - documentazione
     *
     * URL: GET /info
     *
     * @return mappa contenente informazioni base sul servizio
     */
    @GetMapping("/info")
    public Map<String, Object> info() {
        return Map.of(
                "app", "Magazzino API",
                "version", "1.0.0",
                "environment", "dev",
                "author", "Elia Sollazzo"
        );
    }
}