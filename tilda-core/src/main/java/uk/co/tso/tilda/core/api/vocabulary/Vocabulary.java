package uk.co.tso.tilda.core.api.vocabulary;

import org.slf4j.LoggerFactory;
import uk.co.tso.tilda.core.api.vocabulary.property.Property;
import uk.co.tso.tilda.core.exceptions.PropertyNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Vocabulary {
    Map<String, String> namespaces();
    List<Property> properties();

    Optional<String> findPropertyIRIByLabel(String label);

    default String getPropertyIRIByLabel(String label) {
        final var prop = findPropertyIRIByLabel(label);
        if (prop.isEmpty()) {
            LoggerFactory.getLogger(Vocabulary.class).warn("Requested property {} does not exist", label);
        }

        return prop.orElseThrow(PropertyNotFoundException::new);
    }


    default Optional<Property> findPropertyByLabel(String label) {
        return properties().stream()
                .filter(p -> label.equals(p.label()))
                .findFirst();
    }

    default Optional<Property> findPropertyByValue(String value) {
        return properties().stream()
                .filter(p -> value.equals(p.value()))
                .findFirst();
    }

}
