package uk.co.tso.tilda.core.config;

import uk.co.tso.tilda.core.api.API;

public sealed interface APIConfigurationContext extends ConfigurationContext permits EndpointConfigurationContext {
    API api();
}
