package it.spindox.stagelab.magazzino.controllers;
import it.spindox.stagelab.magazzino.dto.users.CreateUserRequestDto;
import it.spindox.stagelab.magazzino.dto.users.CreateUserResponseDto;
import it.spindox.stagelab.magazzino.entities.Utenti;
import it.spindox.stagelab.magazzino.repositories.UtentiRepository;
import it.spindox.stagelab.magazzino.services.UserCreateService;
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

    private final UserCreateService userCreateService;
    private final UtentiRepository utentiRepository;

    // CREA UN UTENTE DI TEST

    @PostMapping("/create")

    public CreateUserResponseDto createUser(@RequestBody CreateUserRequestDto req) {

        Utenti saved = userCreateService.createUser(req.getUsername(), req.getPassword());

        return new CreateUserResponseDto(
                saved.getId(),
                saved.getUsername(),
                OffsetDateTime.now().toString()
        );
    }

    // CREA UTENTI DI TEST


        @PostMapping("/createtestdemo")
        public String createDemoUsers() {

            createIfNotExists("Tom", "Jerry85");
            createIfNotExists("Jerry", "Tom85");
            createIfNotExists("Alexander", "Magnus8");
            createIfNotExists("Dubov", "Alfierec8");

            return "Gli utenti test sono stati creati";
        }

        private void createIfNotExists(String username, String password) {
            if (!utentiRepository.existsByUsername(username)) {
                userCreateService.createUser(username, password);
            }
        }
    }











//   @PostMapping("/createtestdemo")
//        public String createDemoUsers() {
//
//            createIfNotExists("mario", "0000");
//            createIfNotExists("maria", "efesto");
//            createIfNotExists("cicciobello", "zeus87");
//            createIfNotExists("alice", "sarman2004");
//
//            return "Gli utenti test sono stati creati";
//        }
// createIfNotExists("alessandro", "1111");
// createIfNotExists("romina", "1234");
// createIfNotExists("fabio", "zeus88");
// createIfNotExists("omarsj", "sanremo")
//       createIfNotExists("Zio Efesto", "fornace5");
//            createIfNotExists("system", "enigma76");
//            createIfNotExists("elia", "kurama97");
//            createIfNotExists("cr7", "siiiiiiiuuuum");