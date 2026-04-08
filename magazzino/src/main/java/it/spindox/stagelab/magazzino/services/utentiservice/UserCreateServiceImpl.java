package it.spindox.stagelab.magazzino.services.utentiservice;
import it.spindox.stagelab.magazzino.entities.Utenti;
import it.spindox.stagelab.magazzino.repositories.UtentiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


// USERS SERVICE

@Service
@RequiredArgsConstructor
public class UserCreateServiceImpl implements UserCreateService {

    private final UtentiRepository utentiRepository;
    private String ruolo;

    @Override
    public Utenti createUser(String username, String password) {

        if (utentiRepository.existsByUsername(username)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Username già esistente"
            );
        }

        Utenti nuovo = new Utenti();
        nuovo.setUsername(username);
        nuovo.setPassword(password);
        nuovo.setRuolo("USER");
       //  nuovo.setRuolo(ruolo); // USERS O ADMIN A SECONDA DEL RUOLO

        return utentiRepository.save(nuovo);
    }
}