package io.github.thegazette.tilda.core.api.vocabulary.property;

import java.util.Optional;

public interface Property {
    String label();
    String value();
    default String iri() {
        return (value().startsWith("<") ? "" : "<") + value() + (value().endsWith(">") ? "" : ">");
    }
    default Optional<String> range() {
        return Optional.empty();
    }
    default Optional<String> type() {
        return Optional.empty();
    }
    default Optional<String> lang() {
        return Optional.empty();
    }
}
