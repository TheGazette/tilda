package uk.co.tso.tilda.core.config;

import uk.co.tso.tilda.core.api.Configuration;

public sealed interface ConfigurationContext permits APIConfigurationContext {
    Configuration configuration();

}
