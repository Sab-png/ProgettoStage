package it.spindox.stagelab.magazzino.services.authorizzazionservice;
import it.spindox.stagelab.magazzino.dto.login.LoginRequestDto;
import it.spindox.stagelab.magazzino.dto.login.LoginResponseDto;
import it.spindox.stagelab.magazzino.entities.Utenti;
import it.spindox.stagelab.magazzino.mappers.LoginMapper;
import it.spindox.stagelab.magazzino.repositories.UtentiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;



@Service
@RequiredArgsConstructor

public class AuthorizzazionLoginServiceImpl implements AuthorizzazionLoginService {

    private final UtentiRepository utentiRepository;
    private final LoginMapper loginMapper;

    @Override
    public LoginResponseDto login(LoginRequestDto request) {

        Utenti utente = utentiRepository.findByUsernameAndPassword(
                request.getUsername(),
                request.getPassword()
        );

        if (utente == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Credenziali errate"
            );
        }

        // ritorna  al mapper

        return loginMapper.toLoginResponse(utente);
    }
}