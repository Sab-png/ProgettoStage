package it.spindox.stagelab.magazzino.dto.users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor

public class CreateUserRequestDto {
    private String username;
    private String password;
}