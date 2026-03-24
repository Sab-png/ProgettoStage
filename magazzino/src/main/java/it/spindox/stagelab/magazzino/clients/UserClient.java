package it.spindox.stagelab.magazzino.clients;

import it.spindox.stagelab.magazzino.dto.response.PlaceholderUserResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Component
public class UserClient {

    private final WebClient webClient;

    public UserClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://jsonplaceholder.typicode.com")
                .build();
    }

    public Optional<PlaceholderUserResponse> findByEmail(String email) {
        List<PlaceholderUserResponse> users = webClient.get()
                .uri("/users")
                .retrieve()
                .bodyToFlux(PlaceholderUserResponse.class)
                .collectList()
                .block();

        if (users == null) {
            return Optional.empty();
        }

        return users.stream()
                .filter(u -> email.equals(u.getEmail()))
                .findFirst();
    }
}