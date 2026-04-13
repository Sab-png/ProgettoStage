package it.spindox.stagelab.magazzino.dto.response;

import lombok.Data;

@Data
public class CheckoutResponse {

    private String orderId;
    private String message;
    private Double totalAmount;
    private PlaceholderUserResponse user;
}