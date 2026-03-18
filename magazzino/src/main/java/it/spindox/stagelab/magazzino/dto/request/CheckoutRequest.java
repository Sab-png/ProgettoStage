package it.spindox.stagelab.magazzino.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CheckoutRequest {

    @NotBlank(message = "Indirizzo di spedizione obbligatorio")
    private String shippingAddress;

    @NotBlank(message = "Email obbligatoria")
    @Email(message = "Formato email non valido")
    private String shippingEmail;

    @NotBlank(message = "Fascia oraria obbligatoria")
    private String deliveryTimeSlot;

    @NotNull(message = "Data di consegna obbligatoria")
    private LocalDate deliveryDate;
}