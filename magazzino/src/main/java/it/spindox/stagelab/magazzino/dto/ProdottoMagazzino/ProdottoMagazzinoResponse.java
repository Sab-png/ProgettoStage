package it.spindox.stagelab.magazzino.dto.ProdottoMagazzino;
import it.spindox.stagelab.magazzino.entities.StockStatus;
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

    // NUOVI CAMPI (DERIVATI) per colors
    private StockStatus status;
    private String statusColor;
    private String statusDescription;

    public ProdottoMagazzinoResponse(Long id, Long aLong, String s, Long aLong1, String s1, Integer quantita, Object scortaMin) {
    }
}

