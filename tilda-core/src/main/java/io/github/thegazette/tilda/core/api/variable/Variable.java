package io.github.thegazette.tilda.core.api.variable;

import io.github.thegazette.tilda.core.util.URLs;

import java.util.Optional;

public interface Variable {
    String name();
    String value();

    default Optional<String> type() {
        return Optional.empty();
    }

    default Optional<String> lang() {
        return Optional.empty();
    }

    default String sparql() {
        var value = URLs.isURL(value())
                ? "<" + value() + ">"
                : "\"" + value() + "\"";

        if (lang().isPresent()) {
            var lang = lang().map(l -> "@" + l).orElse("");
            return value + lang;
        }
        if (type().isPresent()) {
            var type = type().map(t -> "^^" + t).orElse("");
            return value + type;

        }

        return value;
    }

    default boolean isSameName(Variable other) {
        return this.name().equalsIgnoreCase(other.name());
    }

}
