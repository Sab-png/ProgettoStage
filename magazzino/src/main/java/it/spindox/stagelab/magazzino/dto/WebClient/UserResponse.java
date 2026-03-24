package it.spindox.stagelab.magazzino.dto.WebClient;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;


@JsonPropertyOrder

        ({
        "id",
        "name",
        "username",
        "email",
        "phone",
        "website",
        "address",
        "company"
})

@Data
public class UserResponse {

    private Long id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String website;
    private Address address;
    private Company company;

    // ADDRESS

    @Data
    public static class Address {
        private String street;
        private String suite;
        private String city;
        private String zipcode;
        private Geo geo;
    }

    // POSIZIONE GEOGRAFICA

    @Data
    public static class Geo {
        private String lat;
        private String lng;
    }

    //COMPANIA

    @Data
    public static class Company {
        private String name;
        private String catchPhrase;
        private String bs;
    }
}