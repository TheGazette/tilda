package uk.co.tso.tilda.web.handler.viewers;

import org.springframework.web.servlet.function.ServerRequest;
import uk.co.tso.tilda.core.api.viewer.Viewer;
import uk.co.tso.tilda.core.config.EndpointConfigurationContext;

import java.util.Optional;
import java.util.function.Function;

/**
 * Given a server request, return the viewer for that request
 */
public interface ViewerResolver extends Function<ServerRequest, Optional<Viewer>> {
    interface Factory {
        static ViewerResolver build(EndpointConfigurationContext<?> config) {
            final var endpointViewerResolver = EndpointViewerResolver.Factory.build(config);
            final var apiViewerResolver = ApiViewerResolver.Factory.build(config);
            final var endpointDefaultViewerResolver =
                    EndpointDefaultViewerResolver.Factory.build(config);
            final var apiDefaultViewerResolver = ApiDefaultViewerResolver.Factory.build(config);
            final var defaultViewer = config.configuration().viewer("description");

            return (request) -> {
                if (request.param("_view").isPresent()) {
                    var byEndpoint = endpointViewerResolver.apply(request);
                    if (byEndpoint.isPresent())
                        return byEndpoint;
                    var byApi = apiViewerResolver.apply(request);
                    if (byApi.isPresent())
                        return byApi;
                    //spec says that if _view is present and a viewer by that name can't be found
                    //on either the endpoint or the API then throw a 400 Bad Request
                    throw new RuntimeException();
                }

                var endpointDefault = endpointDefaultViewerResolver.apply(request);
                if (endpointDefault.isPresent())
                    return endpointDefault;

                var apiDefault = apiDefaultViewerResolver.apply(request);
                if (apiDefault.isPresent())
                    return apiDefault;

                return defaultViewer;

            };

        }
    }

}
