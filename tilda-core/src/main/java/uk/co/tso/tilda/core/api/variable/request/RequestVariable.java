package uk.co.tso.tilda.core.api.variable.request;

import uk.co.tso.tilda.core.api.variable.Variable;
import uk.co.tso.tilda.core.api.vocabulary.property.Property;

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
