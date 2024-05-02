package io.github.thegazette.tilda.web.handler.viewers;

import io.github.thegazette.tilda.core.config.EndpointConfigurationContext;

/**
 * Return the default viewer for an endpoint, if defined
 */
public interface EndpointDefaultViewerResolver extends ViewerResolver {
    interface Factory {
        static EndpointDefaultViewerResolver build(EndpointConfigurationContext<?> config){
            final var defaultViewer = config.endpoint().defaultViewer().flatMap(config.configuration()::viewer);
            return (request) -> defaultViewer;
        }
    }
}
