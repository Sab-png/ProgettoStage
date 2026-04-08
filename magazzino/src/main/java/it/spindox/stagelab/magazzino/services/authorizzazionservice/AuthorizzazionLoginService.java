package it.spindox.stagelab.magazzino.services.authorizzazionservice;
import it.spindox.stagelab.magazzino.dto.login.LoginRequestDto;
import it.spindox.stagelab.magazzino.dto.login.LoginResponseDto;


// AUTHORIZZAZIO LOGIN

public interface AuthorizzazionLoginService {
    LoginResponseDto login(LoginRequestDto request);
}
