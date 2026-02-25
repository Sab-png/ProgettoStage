package it.spindox.stagelab.magazzino.dto.prodottomagazzino;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;



@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProdottoMagazzinoRequest {

    @NotNull
    private Long prodottoId;

    @NotNull
    private Long magazzinoId;

    @Min(0)
    private Integer quantita;

    @Min(0)
    private Integer scortaMin;

}