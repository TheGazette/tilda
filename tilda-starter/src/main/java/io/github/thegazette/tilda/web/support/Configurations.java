package io.github.thegazette.tilda.web.support;

import org.eclipse.rdf4j.model.Model;
import io.github.thegazette.tilda.core.api.API;
import io.github.thegazette.tilda.core.api.Configuration;
import io.github.thegazette.tilda.core.api.Endpoint;
import io.github.thegazette.tilda.core.api.Selector;
import io.github.thegazette.tilda.core.api.formatter.Formatter;
import io.github.thegazette.tilda.core.api.viewer.Viewer;
import io.github.thegazette.tilda.core.api.vocabulary.property.Property;
import io.github.thegazette.tilda.core.api.vocabulary.Vocabulary;
import io.github.thegazette.tilda.web.config.properties.TildaProperties;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public interface Configurations {

    static Configuration configuration(final TildaProperties tildaProperties, final Model model) {
        final var apis = APIs.apis(tildaProperties, model);

        final var formatters = Formatters.formatters(tildaProperties, model);
        final var endpoints = Endpoints.endpoints(tildaProperties, model);
        final var selectors = Selectors.selectors(tildaProperties, model);
        final var viewers = Viewers.viewers(tildaProperties, model);
        final var namespaces = Namespaces.namespaces(tildaProperties, model);
        final var properties = Properties.properties(tildaProperties, model);

        final var propertyIRIByLabel = properties.stream()
                .collect(Collectors.toMap(Property::label, Property::iri));

        final var vocabulary = new Vocabulary() {
            @Override
            public Map<String, String> namespaces() {
                return namespaces;
            }

            @Override
            public List<Property> properties() {
                return properties;
            }

            @Override
            public Optional<String> findPropertyIRIByLabel(String label) {
                return Optional.ofNullable(propertyIRIByLabel.get(label));
            }
        };

        return new Configuration() {
            @Override
            public Map<String, API> apis() {
                return apis;
            }

            @Override
            public Map<String, Endpoint> endpoints() {
                return endpoints;
            }

            @Override
            public List<Formatter> formatters() {
                return formatters;
            }

            @Override
            public Map<String, Selector> selectors() {
                return selectors;
            }

            @Override
            public List<Viewer> viewers() {
                return viewers;
            }

            @Override
            public Vocabulary vocabulary() {
                return vocabulary;
            }
        };
    }

}
