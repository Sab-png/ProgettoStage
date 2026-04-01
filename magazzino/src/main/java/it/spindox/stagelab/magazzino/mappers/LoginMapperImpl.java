package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.login.LoginResponseDto;
import it.spindox.stagelab.magazzino.entities.Utenti;
import org.springframework.stereotype.Component;
import java.util.Base64;



@Component
public class LoginMapperImpl implements LoginMapper {

    @Override
    public LoginResponseDto toLoginResponse(Utenti utente) {

        String raw = utente.getUsername() + ":" + utente.getPassword();
        String token = Base64.getEncoder().encodeToString(raw.getBytes());

        return LoginResponseDto.builder()
                .accessToken("Basic " + token)
                .build();
    }
}