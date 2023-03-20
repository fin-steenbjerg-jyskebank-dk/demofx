package dk.stonemountain.demo.demofx.authentication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.HttpExchange;

import dk.stonemountain.demo.demofx.Backend;
import javafx.application.Platform;

public class AuthenticationManager {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationManager.class);
    private static final String CLIENT = "demo";
    private static final String REDIRECT_URL_FORMAT = "http://localhost:%d/authenticate";

    private final Map<String, AuthenticationFlowData> flowData = new HashMap<>();
    private final Webserver webserver = new Webserver(this::handleRequestCallback);
    private final OauthServer oauthServer = new OauthServer();

    private Consumer<AccessTokenResponse> loginHandler;

    private synchronized void startProcess(AuthenticationFlowData data) throws IOException {
        flowData.put(data.state, data);
        data.port = webserver.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }

    private synchronized void stopProcess(AuthenticationFlowData data) {
        flowData.remove(data.state);
        if (flowData.isEmpty()) {
            webserver.stop();
        }
    }

    private synchronized Optional<AuthenticationFlowData> getFlowData(String state) {
        return Optional.ofNullable(flowData.get(state));
    }

    public String initiateAuthenticationFlow(Backend backend, Consumer<AccessTokenResponse> loginHandler) throws IOException {
        this.loginHandler = loginHandler;
        var data = new AuthenticationFlowData();
        data.state = UUID.randomUUID().toString();
        data.pkce = Pkce.generatePkce();
        data.client = CLIENT;
        data.backend = backend;
        startProcess(data);
        data.redirectUrl = REDIRECT_URL_FORMAT.formatted(data.port);
        logger.info("Initiating auth flow at url {} with session data: {}", OauthServer.OIDC_AUTH_URL, data);
        var authUrl = oauthServer.createUrlForAuthCodeWithPkce(data);
        logger.info("Auth url: {}", authUrl);
        return authUrl;
    }

    private void handleRequestCallback(HttpExchange exchange) {
        exchange.getRequestURI().toString();
        UrlParser parser = new UrlParser(exchange.getRequestURI().toString());
        logger.info("Url being handlied: {}", parser);
        var code = parser.getQueryParameter(OauthConstants.CODE);
        var state = parser.getQueryParameter(OauthConstants.STATE);
        if (!code.isPresent() || !state.isPresent()) {
            webserver.callResponse(exchange, "Bad request received", 400);
            return;
        }

        var data = getFlowData(state.get());
        if (!data.isPresent()) {
            webserver.callResponse(exchange, "Authentication process not found", 404);
            return;
        }

        var authenticationFlowData = data.get();
        authenticationFlowData.code = code.get();
        var token = oauthServer.getTokenFromCode(authenticationFlowData);
        if (!token.isPresent()) {
            webserver.callResponse(exchange, "Token was not granted", 412);
            return;
        }

        webserver.callResponse(exchange, "User authenticated", 200);

        Platform.runLater(() -> loginHandler.accept(token.get()));
        stopProcess(authenticationFlowData);
    }

    public static class AuthenticationFlowData {
        Backend backend;
        String client;
        Pkce pkce;
        String state;
        String redirectUrl;
        String code;
        int port;

        @Override
        public String toString() {
            return "AuthenticationFlowData [backend=" + backend + ", client=" + client + ", pkce=" + pkce + ", state=" + state + ", redirectUrl=" + redirectUrl + ", code=" + code + ", port=" + port + "]";
        }
    }
}
