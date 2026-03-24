package it.spindox.stagelab.magazzino.controllers;
import it.spindox.stagelab.magazzino.client.users.UserClient;
import it.spindox.stagelab.magazzino.dto.WebClient.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;



@RestController
@RequiredArgsConstructor

public class TestWebClientController {

    private final UserClient userClient;

    // recupera tutti gli utenti dal servizio esterno

    @GetMapping("/test/webclient/users")
    public List<UserResponse> testGetAllUsers() {
        return userClient.getAllUsers();
    }

    //  recupera un utente per username

    @GetMapping("/test/webclient/user")
    public UserResponse testGetUserByUsername(String username) {
        return userClient.getUserByUsername(username);
    }

    //  recupera un utente per name

    @GetMapping("/test/webclient/user-by-name")
    public UserResponse testUserByName(@RequestParam String name) {
        return userClient.getUserByName(name);
    }
}
