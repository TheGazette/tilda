package uk.co.tso.tilda.web.handler.viewers;

import org.slf4j.LoggerFactory;
import uk.co.tso.tilda.core.config.EndpointConfigurationContext;

import java.util.Optional;

/**
 * if a _view request parameter is present, try to find the corresponding viewer configured on an endpoint
 */
public interface EndpointViewerResolver extends ViewerResolver {
    interface Factory {
        static EndpointViewerResolver build(EndpointConfigurationContext<?> config) {
            final var configuration = config.configuration();
            final var endpoint = config.endpoint();
            var logger = LoggerFactory.getLogger(EndpointViewerResolver.class);

            var endpointViewers = endpoint.viewers().stream()
                    .map(configuration::viewer)
                    .peek(v -> {
                        if (v.isEmpty())
                            logger.warn("Endpoint {} has undefined viewers", endpoint.iri());
                    })
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();


            return (request) -> request.param("_view")
                    .flatMap(name -> endpointViewers.stream().filter(v -> v.matchIRIOrName(name)).findFirst());
        }
    }
}
