package uk.co.tso.tilda.core.config;

import uk.co.tso.tilda.core.api.Endpoint;

public sealed interface EndpointConfigurationContext<E extends Endpoint>
        extends APIConfigurationContext
        permits ItemEndpointConfigurationContext, ItemsEndpointConfigurationContext {

    E endpoint();
}
