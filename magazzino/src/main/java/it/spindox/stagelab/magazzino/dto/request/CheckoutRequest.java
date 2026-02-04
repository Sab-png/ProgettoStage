package it.spindox.stagelab.magazzino.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CheckoutRequest {

    @NotBlank(message = "Email obbligatoria")
    @Email(message = "Formato email non valido")
    private String email;

    @NotBlank(message = "Indirizzo obbligatorio")
    private String shippingAddress;

    @NotBlank(message = "Fascia oraria obbligatoria")
    private String deliveryTimeSlot;
}