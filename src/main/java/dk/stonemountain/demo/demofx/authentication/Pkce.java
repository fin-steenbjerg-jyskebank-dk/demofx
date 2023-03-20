package dk.stonemountain.demo.demofx.authentication;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class Pkce {
    // https://tools.ietf.org/html/rfc7636#section-4.1
    public static final int PKCE_CODE_VERIFIER_MAX_LENGTH = 128;

    private final String codeChallenge;
    private final String codeVerifier;

    public Pkce(String codeVerifier, String codeChallenge) {
        this.codeChallenge = codeChallenge;
        this.codeVerifier = codeVerifier;
    }

    public String getCodeChallenge() {
        return codeChallenge;
    }

    public String getCodeVerifier() {
        return codeVerifier;
    }

    public static Pkce generatePkce() {
        try {
            String codeVerifier = SecretGenerator.getInstance().randomString(PKCE_CODE_VERIFIER_MAX_LENGTH);
            String codeChallenge = generateS256CodeChallenge(codeVerifier);
            return new Pkce(codeVerifier, codeChallenge);
        } catch (Exception ex){
            throw new RuntimeException("Could not generate PKCE", ex);
        }
    }

    // https://tools.ietf.org/html/rfc7636#section-4.6
    private static String generateS256CodeChallenge(String codeVerifier) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(codeVerifier.getBytes(StandardCharsets.ISO_8859_1));
        return encodeBase64ToBase64Url(Base64.getEncoder().encodeToString(md.digest()));
    }

    private static String encodeBase64ToBase64Url(String base64) {
        String s = base64.split("=")[0]; // Remove any trailing '='s
        s = s.replace('+', '-'); // 62nd char of encoding
        s = s.replace('/', '_'); // 63rd char of encoding
        return s;
    }

    @Override
    public String toString() {
        return "Pkce [codeChallenge=" + codeChallenge + ", codeVerifier=" + codeVerifier + "]";
    }
}
