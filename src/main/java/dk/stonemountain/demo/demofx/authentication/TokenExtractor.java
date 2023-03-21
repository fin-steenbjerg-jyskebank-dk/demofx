package dk.stonemountain.demo.demofx.authentication;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Pattern;

import dk.stonemountain.demo.demofx.util.jaxrs.PrivateVisibilityStrategy;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;

public class TokenExtractor {
    private static Jsonb jsonb;
   
    static {
        JsonbConfig config = new JsonbConfig();
        jsonb = JsonbBuilder.newBuilder()
            .withConfig(config)
            .build();
    }
   
    public IdToken extract(String tokenValue) {
        return unmarshallIdToken(getPayload(tokenValue));
    }
   
    private IdToken unmarshallIdToken(String token) {
        return TokenExtractor.jsonb.fromJson(token, IdToken.class);
    }
   
    private String getPayload(String token) {
        String payload = token.split(Pattern.quote("."))[1];
        return new String(Base64.getDecoder().decode(payload), StandardCharsets.UTF_8);
    }
}