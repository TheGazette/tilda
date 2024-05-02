package io.github.thegazette.tilda.core.api.variable.request;

import io.github.thegazette.tilda.core.api.vocabulary.property.Property;
import io.github.thegazette.tilda.core.api.variable.Variable;

import java.util.Optional;

public final class ParamaterVariable implements RequestVariable {
    private final String name;
    private final String value;
    private final Property property;

    private ParamaterVariable(String name, String value, Property property) {
        this.name = name;
        this.value = value;
        this.property = property;
    }

    static public Variable from(String name, String value) {
        return from(name, value, null);
    }

    static public Variable from(String name, String value, Property property) {
        return new ParamaterVariable(name, value, property);
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
