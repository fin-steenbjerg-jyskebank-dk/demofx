package dk.stonemountain.demo.demofx.authentication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class UrlParser {
    private final String context;
    private final Map<String, String> queryParams;

    public UrlParser(String uri) {
        if (uri.contains("?")) {
            context = uri.substring(0, uri.indexOf("?"));
            String params = uri.substring(uri.indexOf("?") + 1);
            queryParams = List.of(params.split("&"))
                .stream()
                .filter(p -> p.contains("="))
                .map(p -> {
                    String[] nameAndValue = p.split("=");
                    return new Pair(nameAndValue);
                })
                .collect(Collectors.toMap(Pair::name, Pair::value));
        } else {
            context = "";
            queryParams = new HashMap<>();
        }
    }    

    public Optional<String> getQueryParameter(String name) {
        return Optional.ofNullable(queryParams.get(name));
    }

    public static record Pair(String name, String value) {
        public Pair(String[] nameAndValue) {
            this(nameAndValue[0], nameAndValue[1]);
        }
    }

    @Override
    public String toString() {
        return "UrlParser [context=" + context + ", queryParams=" + queryParams + "]";
    }
}