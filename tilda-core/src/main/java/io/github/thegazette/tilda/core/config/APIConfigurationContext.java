package io.github.thegazette.tilda.core.config;

import io.github.thegazette.tilda.core.api.API;

public sealed interface APIConfigurationContext extends ConfigurationContext permits EndpointConfigurationContext {
    API api();
}
