package io.github.thegazette.tilda.core.api.vocabulary.property;

import java.util.Optional;

public record ApiProperty(String value, String label, String iri, Optional<String> range) implements Property {
    public static Property from(String value, String label, String iri, Optional<String> range) {
        return new ApiProperty(value, label, iri, range);
    }
}
