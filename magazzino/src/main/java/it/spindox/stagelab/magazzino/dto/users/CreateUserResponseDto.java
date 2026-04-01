package it.spindox.stagelab.magazzino.dto.users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor

public class CreateUserResponseDto {

    private Long id;
    private String username;
    private String createdAt;
}