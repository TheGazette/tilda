package uk.co.tso.tilda.web.handler.variables;

import com.google.common.collect.ImmutableList;
import uk.co.tso.tilda.core.api.variable.Variable;
import uk.co.tso.tilda.core.config.EndpointConfigurationContext;
import uk.co.tso.tilda.web.handler.variables.path.PathVariablesToVariableList;
import uk.co.tso.tilda.web.handler.variables.request.RequestParametersToVariableList;

import java.util.function.Predicate;

public interface EndpointVariableResolver extends VariablesResolver {
    interface Factory {
        static EndpointVariableResolver build(EndpointConfigurationContext<?> config) {
            final var configuration = config.configuration();
            final var api = config.api();
            final var endpoint = config.endpoint();

            final var apiVars = api.variables();
            final var endpointVars = endpoint.variables();

            final var pathVariablesToVariableList =
                    PathVariablesToVariableList.Factory.build(config);
            final var requestParametersToVariableList =
                    RequestParametersToVariableList.Factory.build(config);


            return (request) -> {
                final var pathVars = pathVariablesToVariableList.apply(request.pathVariables());
                final var paramVars = requestParametersToVariableList.apply(request.params());

                final Predicate<Variable> notInEndpointVars = notInVars.apply(endpointVars);
                final Predicate<Variable> notInParamVars = notInVars.apply(paramVars);
                final Predicate<Variable> notInPathVars = notInVars.apply(pathVars);

                //This is going to be horribly inefficient
                return ImmutableList.<Variable>builder()
                        .addAll(endpointVars)
                        .addAll(paramVars.stream().filter(notInEndpointVars).toList())
                        .addAll(pathVars.stream().filter(notInEndpointVars).filter(notInParamVars).toList())
                        .addAll(apiVars.stream().filter(notInEndpointVars).filter(notInParamVars).filter(notInPathVars).toList())
                        .build();
            };
        }
    }
}
