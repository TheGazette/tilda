package io.github.thegazette.tilda.core.api.variable.configuration;

import io.github.thegazette.tilda.core.api.variable.Variable;

public sealed interface ConfigurationVariable extends Variable permits APIVariable, EndpointVariable {
}
