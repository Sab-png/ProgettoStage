package it.spindox.stagelab.magazzino.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CartItemResponse {

    private Long id;
    private Long prodottoId;
    private String prodottoNome;
    private String prodottoImmagine;
    private Double prezzoProdotto;
    private Integer quantity;
    private Double subtotale;
    private LocalDateTime reservedAt;
    private LocalDateTime expiresAt;
    private String status;
    private Long secondsRemaining;
}