package it.spindox.stagelab.magazzino.controllers;
import it.spindox.stagelab.magazzino.dto.login.LoginRequestDto;
import it.spindox.stagelab.magazzino.dto.login.LoginResponseDto;
import it.spindox.stagelab.magazzino.services.AuthorizzazionLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthorizzazionLoginService loginService;


    // LOGIN

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto req) {
        return loginService.login(req);
    }
}
