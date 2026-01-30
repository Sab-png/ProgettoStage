package it.spindox.stagelab.magazzino.dto.ProdottoMagazzino;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdottoMagazzinoResponse {

    private Long id;

    private Long prodottoId;
    private String nomeProdotto;

    private Long magazzinoId;
    private String nomeMagazzino;

    private Integer quantita;
    private Integer scortaMin;
}
