package it.spindox.stagelab.magazzino.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Username obbligatorio")
    private String username;

    @NotBlank(message = "Password obbligatoria")
    private String password;
}