package it.spindox.stagelab.magazzino.dto.prodottomagazzino;
import it.spindox.stagelab.magazzino.entities.ScortaMinPMStatus;
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
    // enum runtime

    private ScortaMinPMStatus scortaMinStatus;

    // valore STRING salvato nel DB

    private String scortaMinStatusDb;
}


