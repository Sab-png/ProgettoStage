package it.spindox.stagelab.magazzino.dto.prodottomagazzino;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProdottoMagazzinoSearchRequest {

    // Filtri
    private Long id;
    private Long prodottoId;
    private Long magazzinoId;

    private Integer quantitaMin;
    private Integer quantitaMax;

    private String nomeProdotto;
    private String nomeMagazzino;

    // Paging (facoltativo: default nel service se null)

    private Integer page;
    private Integer size;
}
