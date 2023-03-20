package dk.stonemountain.demo.demofx.authentication;

import java.util.HashMap;
import java.util.Map;

import jakarta.json.bind.annotation.JsonbProperty;

public class AccessTokenResponse {
    @JsonbProperty("access_token")
    public String token;

    @JsonbProperty("expires_in")
    public long expiresIn;

    @JsonbProperty("refresh_expires_in")
    public long refreshExpiresIn;

    @JsonbProperty("refresh_token")
    public String refreshToken;

    @JsonbProperty("token_type")
    public String tokenType;

    @JsonbProperty("id_token")
    public String idToken;

    @JsonbProperty("not-before-policy")
    public int notBeforePolicy;

    @JsonbProperty("session_state")
    public String sessionState;

    public Map<String, Object> otherClaims = new HashMap<>();

    // OIDC Financial API Read Only Profile : scope MUST be returned in the response from Token Endpoint
    @JsonbProperty("scope")
    public String scope;

    @JsonbProperty("error")
    public String error;

    @JsonbProperty("error_description")
    public String errorDescription;

    @JsonbProperty("error_uri")
    public String errorUri;

    @Override
    public String toString() {
        return "AccessTokenResponse [token=" + token + ", expiresIn=" + expiresIn + ", refreshExpiresIn="
                + refreshExpiresIn + ", refreshToken=" + refreshToken + ", tokenType=" + tokenType + ", idToken="
                + idToken + ", notBeforePolicy=" + notBeforePolicy + ", sessionState=" + sessionState + ", otherClaims="
                + otherClaims + ", scope=" + scope + ", error=" + error + ", errorDescription=" + errorDescription
                + ", errorUri=" + errorUri + "]";
    }
}
