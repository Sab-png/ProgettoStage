package it.spindox.stagelab.magazzino.dto.prodottomagazzino;
import it.spindox.stagelab.magazzino.entities.StockStatusProdotto;
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


    // Stato basato su scortaMin

    private StockStatusProdotto status;
    private String statusColor;
    private String statusDescription;

}