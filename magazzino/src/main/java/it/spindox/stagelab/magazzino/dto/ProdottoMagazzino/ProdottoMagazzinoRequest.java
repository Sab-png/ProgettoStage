package it.spindox.stagelab.magazzino.dto.ProdottoMagazzino;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProdottoMagazzinoRequest {

    @NotNull
    private Long prodottoId;

    @NotNull
    private Long magazzinoId;

    @Min(0)
    private Integer quantita;   // può essere null su update
    @Min(0)
    private Integer scortaMin;  // *Integer*  Scorta Minima
}