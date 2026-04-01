package it.spindox.stagelab.magazzino.services;
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

        return utentiRepository.save(nuovo);
    }
}