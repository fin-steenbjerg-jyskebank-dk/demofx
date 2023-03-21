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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public long getRefreshExpiresIn() {
        return refreshExpiresIn;
    }

    public void setRefreshExpiresIn(long refreshExpiresIn) {
        this.refreshExpiresIn = refreshExpiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public int getNotBeforePolicy() {
        return notBeforePolicy;
    }

    public void setNotBeforePolicy(int notBeforePolicy) {
        this.notBeforePolicy = notBeforePolicy;
    }

    public String getSessionState() {
        return sessionState;
    }

    public void setSessionState(String sessionState) {
        this.sessionState = sessionState;
    }

    public Map<String, Object> getOtherClaims() {
        return otherClaims;
    }

    public void setOtherClaims(Map<String, Object> otherClaims) {
        this.otherClaims = otherClaims;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getErrorUri() {
        return errorUri;
    }

    public void setErrorUri(String errorUri) {
        this.errorUri = errorUri;
    }

    
}
