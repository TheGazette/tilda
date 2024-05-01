package uk.co.tso.tilda.web.handler.viewers;

import org.slf4j.LoggerFactory;
import uk.co.tso.tilda.core.config.APIConfigurationContext;

import java.util.Optional;

public interface ApiViewerResolver extends ViewerResolver {
    interface Factory {
        static ApiViewerResolver build(APIConfigurationContext config) {
            final var configuration = config.configuration();
            final var api = config.api();
            var logger = LoggerFactory.getLogger(ApiViewerResolver.class);
            var apiViewers = api.viewers().stream()
                    .map(configuration::viewer)
                    .peek(v -> {
                        if (v.isEmpty())
                            logger.warn("API {} has undefined viewers", api.iri());
                    })
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();


            return (request) -> request.param("_view")
                    .flatMap(name -> apiViewers.stream().filter(v -> v.matchIRIOrName(name)).findFirst());
        }
    }
}
