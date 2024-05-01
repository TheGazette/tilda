package uk.co.tso.tilda.core.api.variable.configuration;

import uk.co.tso.tilda.core.api.variable.Variable;

public sealed interface ConfigurationVariable extends Variable permits APIVariable, EndpointVariable {
}
