package it.spindox.stagelab.magazzino.configurations.webclient.users;
import it.spindox.stagelab.magazzino.dto.WebClient.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import lombok.extern.slf4j.Slf4j;




@Slf4j
@Service
@RequiredArgsConstructor
public class UserClient {

    private final WebClient userWebClient;


    // Scarica TUTTI gli utenti dal servizio esterno(webclient config. url)


    public List<UserResponse> getAllUsers() {

        try {

            return userWebClient.get()
                    .uri("/users")   // Usa il baseUrl definito nella WebClientConfigurations
                    .retrieve()      // Manda la richiesta e aspetta la risposta
                    .bodyToFlux(UserResponse.class)  // Deserializza ogni oggetto JSON in UserResponse
                    .collectList()   // Converte il flusso in List response
                    .block();        // Esegue
        } catch (Exception e) {
            log.error("ERRORE CHIAMATA WEBCLIENT (getAllUsers): ", e);
            throw e;
        }
    }

    // UserClient chiama un service che scarica tutti gli utenti,
    // e se nella fattura esiste un username, restituisce l’utente corrispondente


    public UserResponse getUserByUsername(String username) {

        // Evita NullPointerException se getAllUsers() ha fallito

        List<UserResponse> users = getAllUsers();
        if (users == null) {
            return null;
        }

        // Filtra la lista degli utenti cercando lo username

        return users.stream()
                .filter(u -> u.getUsername() != null &&
                        u.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null); // Se non trovato da null
    }

    public UserResponse getUserByName(String name) {

        // Evita NullPointerException se getAllUsers ha fallito

        List<UserResponse> users = getAllUsers();
        if (users == null) {
            return null;
        }

        // Filtra la lista degli utenti cercando il campo name

        return users.stream()
                .filter(u -> u.getName() != null &&
                        u.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);   // se non corrisonde  null
    }
}


