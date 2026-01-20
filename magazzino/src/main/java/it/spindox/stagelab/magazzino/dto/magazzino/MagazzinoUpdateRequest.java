
package it.spindox.stagelab.magazzino.dto.magazzino;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class MagazzinoUpdateRequest {

    private String nome;

    private String descrizione;

    @Min(value = 0, message = "La capacità deve essere >= 0")
    private Integer capacita;
}

