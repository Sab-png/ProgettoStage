package it.spindox.stagelab.magazzino.dto.response;

import lombok.Data;

@Data
public class AddressResponse {

    private String street;
    private String suite;
    private String city;
    private String zipcode;
    private GeoResponse geo;
}