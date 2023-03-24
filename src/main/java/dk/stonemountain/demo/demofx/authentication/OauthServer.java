package dk.stonemountain.demo.demofx.authentication;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.stonemountain.demo.demofx.util.jaxrs.JsonbHelper;

public class OauthServer {
    private static final Logger log = LoggerFactory.getLogger(OauthServer.class);
	public static final String OIDC_AUTH_URL = "https://auth.stonemountain.dk/realms/demo/protocol/openid-connect/auth";
	public static final String OAUTH_URL = "https://auth.stonemountain.dk/realms/demo/protocol/openid-connect/token";

    public String createUrlForAuthCodeWithPkce(AuthenticationManager.AuthenticationFlowData data) {
        var redirectUrl = createQueryParam(OauthConstants.REDIRECT_URI, data.redirectUrl);
        var state = createQueryParam(OauthConstants.STATE, data.state);
        var challenge = createQueryParam(OauthConstants.CODE_CHALLENGE, data.pkce.getCodeChallenge());
        var challengeMethod = createQueryParam(OauthConstants.CODE_CHALLENGE_METHOD, OauthConstants.PKCE_METHOD_S256);
        var client = createQueryParam(OauthConstants.CLIENT_ID, data.client);
        var responseType = createQueryParam(OauthConstants.RESPONSE_TYPE, OauthConstants.CODE);
        var scope = createQueryParam(OauthConstants.SCOPE, OauthConstants.SCOPE_OPENID);

        return OIDC_AUTH_URL 
            + "?"
            + client
            + "&"
            + redirectUrl
            + "&"
            + scope
            + "&"
            + responseType
            + "&"
            + challenge     
            + "&"
            + challengeMethod
            + "&"
            + state;
    }

    public Optional<AccessTokenResponse> getTokenFromCode(AuthenticationManager.AuthenticationFlowData data) {
        var redirectUrl = createQueryParam(OauthConstants.REDIRECT_URI, data.redirectUrl);
        var grantType = createQueryParam(OauthConstants.GRANT_TYPE, OauthConstants.AUTHORIZATION_CODE);
        var codeVerifier = createQueryParam(OauthConstants.CODE_VERIFIER, data.pkce.getCodeVerifier());
        var code = createQueryParam(OauthConstants.CODE, data.code);
        var client = createQueryParam(OauthConstants.CLIENT_ID, data.client);

        var body = grantType + "&" + client + "&" + code + "&" + codeVerifier + "&" + redirectUrl;

        return invokeOauthServer(OAUTH_URL, body);
    }

    public Optional<AccessTokenResponse> getTokenFromRefresh(String client, String refreshToken) {
        var clientId = createQueryParam("client_id", client);
        var grant = createQueryParam("grant_type", "refresh_token");
        var token = createQueryParam("refresh_token", refreshToken);

        var body = grant +"&" + clientId + "&" + token;

        return invokeOauthServer(OAUTH_URL, body);
    }
    
    private String createQueryParam(String param, String value) {
        return param + "=" + URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private Optional<AccessTokenResponse> invokeOauthServer(String url, String body) {
        Optional<AccessTokenResponse> result = Optional.empty();

        try {
			HttpClient client = HttpClient.newBuilder()
				.connectTimeout(Duration.ofSeconds(15))
				.build();
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(url))
                    .header("content-type", "application/x-www-form-urlencoded")
					.timeout(Duration.ofSeconds(30))
					.POST(BodyPublishers.ofString(body))
					.build();

			log.trace("Connecting to {}", url);
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			var statusCode = response.statusCode();
			log.trace("Oauth server status code {}", statusCode);
			if (statusCode == 200) {
                if (log.isTraceEnabled()) {
                    log.trace("Received from ouath server: {}", response.body());
                }
				result = Optional.of(JsonbHelper.fromJson(response.body(), AccessTokenResponse.class));
				log.trace("Fetched token: {}", result);
			} else {
				log.error("Failed to get token from url {} with body {}. Status code {}", url, body, statusCode);
			}
		} catch (Exception e) {
			log.error("Access token retrievel failed from url {} and body {}", url, body, e);
		}

        return result;

    }
}
