package io.github.thegazette.tilda.core.api.variable.request;

import io.github.thegazette.tilda.core.api.vocabulary.property.Property;
import io.github.thegazette.tilda.core.api.variable.Variable;

import java.util.Optional;

public interface RequestVariable extends Variable {
    default Optional<Property> property() {
        return Optional.empty();
    }

    @Override
    default Optional<String> type() {
        return property().flatMap(Property::range);
    }
}
