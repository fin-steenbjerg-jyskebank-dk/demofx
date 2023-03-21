package dk.stonemountain.demo.demofx.authentication;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;

public record AccessTokenResponse(String token, long expiresIn, long refreshExpiresIn, String refreshToken, String tokenType, String idToken, int notBeforePolicy, String sessionState, String scope) {
    @JsonbCreator
    public static AccessTokenResponse create(
        @JsonbProperty("access_token") String token,
        @JsonbProperty("expires_in") long expiresIn,
        @JsonbProperty("refresh_expires_in") long refreshExpiresIn,
        @JsonbProperty("refresh_token") String refreshToken,
        @JsonbProperty("token_type") String tokenType,
        @JsonbProperty("id_token") String idToken,
        @JsonbProperty("not-before-policy") int notBeforePolicy,
        @JsonbProperty("session_state") String sessionState,
        @JsonbProperty("scope") String scope) {
            return new AccessTokenResponse(token, expiresIn, refreshExpiresIn, refreshToken, tokenType, idToken, notBeforePolicy, sessionState, scope);
        }   
}
