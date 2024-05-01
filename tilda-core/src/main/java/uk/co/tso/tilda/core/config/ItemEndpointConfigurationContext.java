package uk.co.tso.tilda.core.config;

import uk.co.tso.tilda.core.api.API;
import uk.co.tso.tilda.core.api.Configuration;
import uk.co.tso.tilda.core.api.Endpoint;

public record ItemEndpointConfigurationContext(Configuration configuration, API api, Endpoint.Item endpoint)
            implements EndpointConfigurationContext<Endpoint.Item> {

    public static ItemEndpointConfigurationContext build(Configuration configuration, API api, Endpoint.Item endpoint) {
        return new ItemEndpointConfigurationContext(configuration, api, endpoint);
    }
}
