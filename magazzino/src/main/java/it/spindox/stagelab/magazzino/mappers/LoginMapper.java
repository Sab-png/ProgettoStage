package it.spindox.stagelab.magazzino.mappers;
import it.spindox.stagelab.magazzino.dto.login.LoginResponseDto;
import it.spindox.stagelab.magazzino.entities.Utenti;



public interface LoginMapper {

    LoginResponseDto toLoginResponse(Utenti utente);
}