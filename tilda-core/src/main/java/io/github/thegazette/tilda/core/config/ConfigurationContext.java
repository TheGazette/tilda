package io.github.thegazette.tilda.core.config;

import io.github.thegazette.tilda.core.api.Configuration;

public sealed interface ConfigurationContext permits APIConfigurationContext {
    Configuration configuration();

}
