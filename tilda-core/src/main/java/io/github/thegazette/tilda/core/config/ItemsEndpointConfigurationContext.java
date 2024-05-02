package io.github.thegazette.tilda.core.config;

import io.github.thegazette.tilda.core.api.API;
import io.github.thegazette.tilda.core.api.Configuration;
import io.github.thegazette.tilda.core.api.Endpoint;

public record ItemsEndpointConfigurationContext(Configuration configuration, API api, Endpoint.Items endpoint)
                    implements EndpointConfigurationContext<Endpoint.Items> {

    public static ItemsEndpointConfigurationContext build(Configuration configuration, API api, Endpoint.Items endpoint) {
        return new ItemsEndpointConfigurationContext(configuration, api, endpoint);
    }
}
