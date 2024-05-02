package io.github.thegazette.tilda.core.config;

import io.github.thegazette.tilda.core.api.Endpoint;

public sealed interface EndpointConfigurationContext<E extends Endpoint>
        extends APIConfigurationContext
        permits ItemEndpointConfigurationContext, ItemsEndpointConfigurationContext {

    E endpoint();
}
