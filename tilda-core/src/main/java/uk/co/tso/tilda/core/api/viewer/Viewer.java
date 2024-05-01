package uk.co.tso.tilda.core.api.viewer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface Viewer {
    String iri();
    String name();

    //According to the "standard", there's a template property, but none of the gazettes Views have
    //one, instead they have 0..m "graphTemplate"s, which are not mentioned anywhere
    default Optional<String> template() {
        return Optional.empty();
    }
    default List<String> graphTemplates() {
        return Collections.emptyList();
    }

    default List<String> property() {
        return Collections.emptyList();
    }

    default Optional<String> propertyChain() {
        return Optional.empty();
    }

    default List<String> include() {
        return Collections.emptyList();
    }

    default boolean matchIRIOrName(String value) {
        return value != null && !value.isEmpty() && (value.equalsIgnoreCase(iri()) || value.equalsIgnoreCase(name()));
    }
}
