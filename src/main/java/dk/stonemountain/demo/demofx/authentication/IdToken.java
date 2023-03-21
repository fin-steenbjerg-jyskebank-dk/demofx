package dk.stonemountain.demo.demofx.authentication;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

public record IdToken (String userId, String userName, String email) {
    @JsonbCreator
    public static IdToken create(
        @JsonbProperty("preferred_username") String userId,
        @JsonbProperty("name") String userName,
        @JsonbProperty("email") String email
    ) {
        return new IdToken(userId, userName, email);
    }
}
