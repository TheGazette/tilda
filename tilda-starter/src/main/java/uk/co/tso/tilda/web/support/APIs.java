package uk.co.tso.tilda.web.support;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.tso.tilda.core.api.API;
import uk.co.tso.tilda.core.api.ContentNegotiation;
import uk.co.tso.tilda.core.util.Constants;
import uk.co.tso.tilda.core.util.Optionals;
import uk.co.tso.tilda.web.config.properties.APIProperties;
import uk.co.tso.tilda.web.config.properties.TildaProperties;
import uk.co.tso.tilda.core.exceptions.NoSPARQLEndpointDefinedException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static uk.co.tso.tilda.web.support.Common.name;

public interface APIs {
    static Optional<String> sparqlEndpoint(final Model model, final Resource iri) {
        return Util.objectValue(model, iri, Constants.SPARQL_ENDPOINT);
    }

    static Optional<String> sparqlEndpoint(final APIProperties properties, final Model model, final Resource iri) {
        return Optionals.eitherOr(properties.findSparqlEndpoint(), sparqlEndpoint(model, iri));
    }

    static Optional<String> base(final Model model, final Resource iri) {
        return Util.objectValue(model, iri, Constants.BASE);
    }

    static Optional<String> base(final APIProperties properties, final Model model, final Resource iri) {
        return Optionals.eitherOr(properties.findBase(), base(model, iri));
    }

    static Optional<String> contentNegotiation(final Model model, final Resource iri) {
        return Util.objectValue(model, iri, Constants.CONTENT_NEGOTIATION);
    }

    static Optional<String> contentNegotiation(final APIProperties properties, final Model model, final Resource iri) {
        return Optionals.eitherOr(properties.findContentNegotiation(), contentNegotiation(model, iri));
    }

    static Optional<String> defaultPageSize(final Model model, final Resource iri) {
        return Util.objectValue(model, iri, Constants.DEFAULT_PAGE_SIZE);
    }

    static Optional<String> defaultPageSize(final APIProperties properties, final Model model, final Resource iri) {
        return Optionals.eitherOr(properties.findDefaultPageSize(), defaultPageSize(model, iri));
    }

    static Optional<String> maxPageSize(final Model model, final Resource iri) {
        return Util.objectValue(model, iri, Constants.MAX_PAGE_SIZE);
    }

    static Optional<String> maxPageSize(final APIProperties properties, final Model model, final Resource iri) {
        return Optionals.eitherOr(properties.findMaxPageSize(), maxPageSize(model, iri));
    }

    static API api(final TildaProperties tildaProperties, final Model config, final Resource iri) {
        Logger logger = LoggerFactory.getLogger(APIs.class);

        final var name = name(config, iri);
        final var properties = tildaProperties.api(name);
        final var sparqlEndpoint = sparqlEndpoint(properties, config, iri).orElseThrow(NoSPARQLEndpointDefinedException::new);
        final var base = base(properties,config, iri);
        final var contentNegotiation = contentNegotiation(properties, config, iri).map(ContentNegotiation::of).orElse(ContentNegotiation.SUFFIX);
        if (contentNegotiation == ContentNegotiation.SUFFIX) {
            logger.warn("File type suffix content negotiation is enabled. This is not recommended");
        }

        //config.filter(null, Constants.MAX_PAGE_SIZE, null).stream().forEach();

        final var lang = Lists.eitherOr(properties.getLang(), Common.lang(config, iri));

        final var comment = Common.comment(config, iri);
        final var label = Common.labelRDFS(config, iri);

        final var maxPageSize = maxPageSize(properties, config, iri);
        final var defaultPageSize = defaultPageSize(properties, config, iri);

        final var endpoints = config.filter(iri, Constants.ENDPOINT, null).stream()
                .map(Statement::getObject)
                .map(Value::stringValue)
                .toList();

        final var viewers = config.filter(iri, Constants.VIEWER, null).stream()
                .map(Statement::getObject)
                .map(Value::stringValue)
                .toList();

        final var defaultFormatter = Util.objectResource(config, iri, Constants.DEFAULT_FORMATTER).map(Resource::stringValue);

        return new API() {
            @Override
            public String iri() {
                return iri.stringValue();
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public Optional<String> label() {
                return label;
            }

            @Override
            public Optional<String> comment() {
                return comment;
            }

            @Override
            public String sparqlEndpoint() {
                return sparqlEndpoint;
            }

            @Override
            public Optional<String> base() {
                return base;
            }

            @Override
            public ContentNegotiation contentNegotiation() {
                return contentNegotiation;
            }

            @Override
            public List<String> lang() {
                return null;
            }

            @Override
            public Optional<String> maxPageSize() {
                return maxPageSize;
            }

            @Override
            public Optional<String> defaultPageSize() {
                return defaultPageSize;
            }

            @Override
            public Optional<String> defaultViewer() {
                return Optional.empty();
            }

            @Override
            public List<String> vocabularies() {
                return null;
            }

            @Override
            public List<String> declaredEndpoints() {
                return null;
            }

            @Override
            public List<String> endpoints() {
                return endpoints;
            }

            @Override
            public Optional<String> defaultFormatter() {
                return defaultFormatter;
            }

            @Override
            public List<String> viewers() {
                return viewers;
            }
        };
    }

    static Map<String, API> apis(final TildaProperties tildaProperties, final Model model) {
        return model
                .filter(null, Constants.RDF_TYPE, Constants.API_CLASS)
                .stream()
                .map(Statement::getSubject)
                .map(iri -> api(tildaProperties, model, iri))
                .collect(Collectors.toMap(API::iri, Function.identity()));
    }
}
