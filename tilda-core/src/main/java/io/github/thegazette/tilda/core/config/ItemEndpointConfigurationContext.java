package io.github.thegazette.tilda.core.config;

import io.github.thegazette.tilda.core.api.API;
import io.github.thegazette.tilda.core.api.Configuration;
import io.github.thegazette.tilda.core.api.Endpoint;

public record ItemEndpointConfigurationContext(Configuration configuration, API api, Endpoint.Item endpoint)
            implements EndpointConfigurationContext<Endpoint.Item> {

    public static ItemEndpointConfigurationContext build(Configuration configuration, API api, Endpoint.Item endpoint) {
        return new ItemEndpointConfigurationContext(configuration, api, endpoint);
    }
}
