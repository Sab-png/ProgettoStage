package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.users.CreateUserResponseDto;
import it.spindox.stagelab.magazzino.entities.Utenti;
import org.springframework.stereotype.Component;
import java.time.OffsetDateTime;


@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public CreateUserResponseDto toCreateUserResponse(Utenti utente) {

        return new CreateUserResponseDto(
                utente.getId(),
                utente.getUsername(),
                OffsetDateTime.now().toString()
        );
    }
}