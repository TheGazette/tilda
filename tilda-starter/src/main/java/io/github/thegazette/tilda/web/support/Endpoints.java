package io.github.thegazette.tilda.web.support;

import com.google.common.collect.ImmutableList;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import io.github.thegazette.tilda.core.api.Endpoint;
import io.github.thegazette.tilda.core.util.Constants;
import io.github.thegazette.tilda.core.util.Optionals;
import io.github.thegazette.tilda.web.config.properties.TildaProperties;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.github.thegazette.tilda.web.support.Common.nameForSubject;
import static io.github.thegazette.tilda.web.support.Common.nameFromSubject;

public interface Endpoints {

    record ItemEndpoint(
            String iri,
            String uriTemplate,
            Optional<String> comment,
            Optional<String> defaultPageSize,
            Optional<String> exampleRequestPath,
            List<String> lang,
            List<String> viewers,
            Optional<String> defaultViewer,
            List<String> formatters,
            Optional<String> defaultFormatter,
            String itemTemplate) implements Endpoint.Item {
        static ItemEndpoint from(final TildaProperties tildaProperties, final Model config, final Resource iri) {
            var name = nameForSubject(config, iri).orElse(nameFromSubject(iri));
            var properties = tildaProperties.endpoint(name);

            return new ItemEndpoint(
                    Common.iri(iri),
                    Optionals.eitherOr(properties.findUriTemplate(), Endpoints.uriTemplate(config, iri)).orElse(""),
                    Optionals.eitherOr(properties.findComment(), Common.comment(config, iri)),
                    Optionals.eitherOr(properties.findExampleRequestPath(), Endpoints.exampleRequestPath(config, iri)),
                    Optionals.eitherOr(properties.findDefaultPageSize(), Endpoints.defaultPageSize(config, iri)),
                    Lists.eitherOr(properties.getLang(), Common.lang(config, iri)),
                    Lists.eitherOr(properties.getViewers(), Endpoints.viewers(config, iri)),
                    Optionals.eitherOr(properties.findDefaultViewer(), Endpoints.defaultViewer(config, iri)),
                    Lists.eitherOr(properties.getFormatters(), Endpoints.formatter(config, iri)),
                    Optionals.eitherOr(properties.findDefaultFormatter(), Endpoints.defaultFormatter(config, iri)),
                    Optionals.eitherOr(properties.findItemTemplate(), Endpoints.itemTemplate(config, iri)).orElse("")
            );

        }
    }

    record ListEndpoint(String iri,
                        String uriTemplate,
                        Optional<String> comment,
                        Optional<String> exampleRequestPath,
                        Optional<String> defaultPageSize,
                        List<String> lang,
                        List<String> viewers,
                        List<String> formatters,
                        Optional<String> defaultFormatter,
                        String selector) implements Endpoint.Items{
        static ListEndpoint from(final TildaProperties tildaProperties, final Model config, final Resource iri) {
            var name = nameForSubject(config, iri).orElse(nameFromSubject(iri));
            var properties = tildaProperties.endpoint(name);

            return new ListEndpoint(
                    Common.iri(iri),
                    Optionals.eitherOr(properties.findUriTemplate(), Endpoints.uriTemplate(config, iri)).orElse(""),
                    Optionals.eitherOr(properties.findComment(), Common.comment(config, iri)),
                    Optionals.eitherOr(properties.findExampleRequestPath(), Endpoints.exampleRequestPath(config, iri)),
                    Optionals.eitherOr(properties.findDefaultPageSize(), Endpoints.defaultPageSize(config, iri)),
                    Lists.eitherOr(properties.getLang(), Common.lang(config, iri)),
                    Lists.eitherOr(properties.getViewers(), Endpoints.viewers(config, iri)),
                    Lists.eitherOr(properties.getFormatters(), Endpoints.formatter(config, iri)),
                    Optionals.eitherOr(properties.findDefaultFormatter(), Endpoints.defaultFormatter(config, iri)),
                    Optionals.eitherOr(properties.findSelector(), Endpoints.selector(config, iri)).orElse("")
            );
        }
    }


    static List<ItemEndpoint> itemEndpoints(final TildaProperties tildaProperties, final Model config) {
        return config.filter(null, null, Constants.ITEM_ENDPOINT_CLASS)
                .stream()
                .map(Statement::getSubject)
                .map(iri -> ItemEndpoint.from(tildaProperties, config, iri))
                .toList();
    }

    static List<ListEndpoint> listEndpoints(final TildaProperties tildaProperties, final Model config) {
        return config.filter(null, null, Constants.LIST_ENDPOINT_CLASS)
                .stream()
                .map(Statement::getSubject)
                .map(iri -> ListEndpoint.from(tildaProperties, config, iri))
                .toList();
    }

    static Map<String, Endpoint> endpoints(final TildaProperties tildaProperties, final Model config) {
        return ImmutableList.<Endpoint>builder()
                .addAll(Endpoints.itemEndpoints(tildaProperties, config))
                .addAll(Endpoints.listEndpoints(tildaProperties, config))
                .build()
                .stream()
                .collect(Collectors.toMap(Endpoint::iri, Function.identity()));
    }

    static Optional<String> uriTemplate(Model model, Resource subject) {
        return Util.objectValue(model, subject, Constants.URI_TEMPLATE);
    }

    static Optional<String> exampleRequestPath(Model model, Resource subject) {
        return Util.objectValue(model, subject, Constants.EXAMPLE_REQUEST_PATH);
    }

    static Optional<String> defaultPageSize(Model model, Resource subject) {
        return Util.objectValue(model, subject, Constants.DEFAULT_PAGE_SIZE);
    }

    static Optional<String>  itemTemplate(Model model, Resource subject) {
        return Util.objectValue(model, subject, Constants.ITEM_TEMPLATE);
    }

    static Optional<String> selector(Model model, Resource subject) {
        return Util.objectValue(model, subject, Constants.SELECTOR);
    }

    static List<String> viewers(Model model, Resource subject) {
        return Util.objectResources(model, subject, Constants.VIEWER)
                .stream()
                .map(Resource::stringValue)
                .toList();
    }

    static Optional<String> defaultViewer(Model model, Resource subject) {
        return Util.objectResource(model, subject, Constants.DEFAULT_VIEWER).map(Resource::stringValue);
    }

    static List<String> formatter(Model model, Resource subject) {
        return Util.objectResources(model, subject, Constants.FORMATTER)
                .stream()
                .map(Resource::stringValue)
                .toList();
    }

    static Optional<String> defaultFormatter(Model model, Resource subject) {
        return Util.objectResource(model, subject, Constants.DEFAULT_FORMATTER).map(Resource::stringValue);
    }
}
