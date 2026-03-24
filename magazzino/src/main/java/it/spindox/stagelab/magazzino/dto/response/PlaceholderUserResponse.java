package it.spindox.stagelab.magazzino.dto.response;

import lombok.Data;

@Data
public class PlaceholderUserResponse {

    private Long id;
    private String name;
    private String username;
    private String email;
    private AddressResponse address;
    private String phone;
    private String website;
    private CompanyResponse company;
}