package it.spindox.stagelab.magazzino.controllers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.Instant;
import java.util.Map;


@RestController
@RequestMapping("/home")

public class HomeController {


     //Homepage del servizio.
     //URL: GET /home

    @GetMapping

    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Magazzino API Running ☻");
    }


     // Health check endpoint.
      // Restituisce stato e timestamp corrente.
      // URL: GET /home/health

    @GetMapping("/health")

    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = Map.of(
                "status", "UP",
                "timestamp", Instant.now().toString()
        );
        return ResponseEntity.ok(response);
    }


     // Informazioni base sul servizio.
     // URL: GET /home/info

    @GetMapping("/info")

    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> response = Map.of(
                "app", "Magazzino API",
                "version", "1.0.0",
                "environment", "dev",
                "author", "Elia Sollazzo"
        );
        return ResponseEntity.ok(response);
    }
}