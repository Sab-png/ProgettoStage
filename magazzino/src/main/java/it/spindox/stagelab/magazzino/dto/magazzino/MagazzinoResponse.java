
package it.spindox.stagelab.magazzino.dto.magazzino;
import it.spindox.stagelab.magazzino.entities.StockStatusMagazzino;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MagazzinoResponse {

    private Long id;
    private String nome;
    private String indirizzo;
    private Integer capacita;

    // Campi SEARCH

    private Integer quantitaTotale;
    private Double percentuale;

    private StockStatusMagazzino stockStatus;

    private String statusColor;
    private String statusDescription;
}