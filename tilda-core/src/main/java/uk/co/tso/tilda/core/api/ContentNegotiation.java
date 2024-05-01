package uk.co.tso.tilda.core.api;


import java.util.Optional;

public enum ContentNegotiation {
    SUFFIX,
    PARAMETER;

    public static ContentNegotiation of(String value) {
        return Optional.ofNullable(value)
                .map(String::toLowerCase)
                .map(s -> s.replace("based", ""))
                .map(s -> "parameter".equals(s) ? PARAMETER : SUFFIX)
                .orElse(SUFFIX);
    }


}
