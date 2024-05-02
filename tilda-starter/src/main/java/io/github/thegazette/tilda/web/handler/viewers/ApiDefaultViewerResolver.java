package io.github.thegazette.tilda.web.handler.viewers;

import io.github.thegazette.tilda.core.config.APIConfigurationContext;

public interface ApiDefaultViewerResolver extends ViewerResolver {
    interface Factory {
        static ApiDefaultViewerResolver build(APIConfigurationContext config){
            final var configuration = config.configuration();
            final var api = config.api();
            final var defaultViewer = api.defaultViewer().flatMap(configuration::viewer);
            return (request) -> defaultViewer;
        }
    }
}
