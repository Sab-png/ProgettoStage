package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.users.CreateUserResponseDto;
import it.spindox.stagelab.magazzino.entities.Utenti;



public interface UserMapper {

    CreateUserResponseDto toCreateUserResponse(Utenti utente);
}