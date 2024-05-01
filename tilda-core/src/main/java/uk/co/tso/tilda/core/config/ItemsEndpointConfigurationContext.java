package uk.co.tso.tilda.core.config;

import uk.co.tso.tilda.core.api.API;
import uk.co.tso.tilda.core.api.Configuration;
import uk.co.tso.tilda.core.api.Endpoint;

public record ItemsEndpointConfigurationContext(Configuration configuration, API api, Endpoint.Items endpoint)
                    implements EndpointConfigurationContext<Endpoint.Items> {

    public static ItemsEndpointConfigurationContext build(Configuration configuration, API api, Endpoint.Items endpoint) {
        return new ItemsEndpointConfigurationContext(configuration, api, endpoint);
    }
}
