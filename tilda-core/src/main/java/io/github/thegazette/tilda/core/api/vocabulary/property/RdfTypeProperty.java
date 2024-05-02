package io.github.thegazette.tilda.core.api.vocabulary.property;

import io.github.thegazette.tilda.core.util.Constants;

public record RdfTypeProperty(String label) implements Property {
    public static Property from(final String label) {
        return new RdfTypeProperty(label);
    }
    @Override
    public String value() {
        return Constants.RDF.TYPE;
    }
}
