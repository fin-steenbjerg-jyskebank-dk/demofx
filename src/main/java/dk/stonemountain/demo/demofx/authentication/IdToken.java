package dk.stonemountain.demo.demofx.authentication;

import jakarta.json.bind.annotation.JsonbProperty;

public class IdToken {
    @JsonbProperty("preferred_username")
    public String userId;
    @JsonbProperty("name")
    public String userName;
    @JsonbProperty("email")
    public String email;
}
