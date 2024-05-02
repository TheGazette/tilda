package io.github.thegazette.tilda.core.api.variable.request;

import io.github.thegazette.tilda.core.api.vocabulary.property.Property;
import io.github.thegazette.tilda.core.api.variable.Variable;

import java.util.Optional;

public final class PathVariable implements RequestVariable {
    final String name;
    final String value;
    final Property property;

    private PathVariable(String name, String value, Property property) {
        this.name = name;
        this.value = value;
        this.property = property;
    }

    public static Variable from(String name, String value, Property property) {
        return new PathVariable(name, value, property);
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public Optional<Property> property() {
        return Optional.ofNullable(property);
    }
}
