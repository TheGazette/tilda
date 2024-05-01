package uk.co.tso.tilda.web.handler.viewers;

import uk.co.tso.tilda.core.config.EndpointConfigurationContext;

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
