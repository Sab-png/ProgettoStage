package it.spindox.stagelab.magazzino.controllers;
import it.spindox.stagelab.magazzino.dto.users.CreateUserRequestDto;
import it.spindox.stagelab.magazzino.dto.users.CreateUserResponseDto;
import it.spindox.stagelab.magazzino.entities.Utenti;
import it.spindox.stagelab.magazzino.repositories.UtentiRepository;
import it.spindox.stagelab.magazzino.services.utentiservice.AdminCreateService;
import it.spindox.stagelab.magazzino.services.utentiservice.UserCreateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.OffsetDateTime;






@RestController
@RequestMapping("/users")
@RequiredArgsConstructor

public class UserCreateController {

    private final UserCreateService userCreateService;      // crea USER
    private final AdminCreateService adminCreateService;    // crea ADMIN
    private final UtentiRepository utentiRepository;

    //  CREA UTENTE NORMALE (USER)

    @PostMapping("/create")
    public CreateUserResponseDto createUser(@RequestBody CreateUserRequestDto req) {

        Utenti saved = userCreateService.createUser(
                req.getUsername(),
                req.getPassword()
        );

        return new CreateUserResponseDto(
                saved.getId(),
                saved.getUsername(),
                OffsetDateTime.now().toString()
        );
    }

    //  CREA ADMIN (ADMIN)

    @PostMapping("/create-admin")
    public CreateUserResponseDto createAdmin(@RequestBody CreateUserRequestDto req) {

        Utenti saved = adminCreateService.createAdmin(
                req.getUsername(),
                req.getPassword()
        );

        return new CreateUserResponseDto(
                saved.getId(),
                saved.getUsername(),
                OffsetDateTime.now().toString()
        );
    }

    //  CREA UTENTI DI TEST PER GLI USERS

    @PostMapping("/createtestdemo")
    public String createDemoUsers() {

        createIfNotExists("Tom", "Jerry85");
        createIfNotExists("Jerry", "Tom85");
        createIfNotExists("Alexander", "Magnus8");
        createIfNotExists("Dubov", "Alfierec8");

        return "Gli utenti test sono stati creati (tutti USER)";
    }

    // evitare duplicati PER GLI USERS  posso implementare

    private void createIfNotExists(String username, String password) {
        if (!utentiRepository.existsByUsername(username)) {
            userCreateService.createUser(username, password); // crea USER
        }
    }
}