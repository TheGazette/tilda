package uk.co.tso.tilda.core.api.vocabulary.property;

import uk.co.tso.tilda.core.util.Constants;

public record RdfTypeProperty(String label) implements Property {
    public static Property from(final String label) {
        return new RdfTypeProperty(label);
    }
    @Override
    public String value() {
        return Constants.RDF.TYPE;
    }
}
