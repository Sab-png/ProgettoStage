package it.spindox.stagelab.magazzino.services.utentiservice;
import it.spindox.stagelab.magazzino.entities.Utenti;
import it.spindox.stagelab.magazzino.repositories.UtentiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


// Classe per le inf sugli admin

@Service
@RequiredArgsConstructor


public class AdminCreateServiceImpl implements AdminCreateService {

    private final UtentiRepository utentiRepository;

    @Override
    public Utenti createAdmin(String username, String password) {

        if (utentiRepository.existsByUsername(username)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Username già esistente"
            );
        }

        Utenti admin = new Utenti();
        admin.setUsername(username);
        admin.setPassword(password);
        admin.setRuolo("ADMIN"); // ruolo admin

        return utentiRepository.save(admin);
    }
}