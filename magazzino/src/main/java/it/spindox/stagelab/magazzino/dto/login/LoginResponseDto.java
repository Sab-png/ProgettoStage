package it.spindox.stagelab.magazzino.dto.login;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class LoginResponseDto {

private String accessToken;  // un esempio puo' essere :  "Basic ZWxpYToxMjM0" decriptato
}













