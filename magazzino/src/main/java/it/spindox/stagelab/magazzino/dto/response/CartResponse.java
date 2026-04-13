package it.spindox.stagelab.magazzino.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class CartResponse {

    private List<CartItemResponse> items;
    private Integer totalItems;
    private Double totalPrice;
    private Long secondsRemaining;
    private Boolean cartActive;
    private Long magazzinoId;
    private PlaceholderUserResponse user;
}