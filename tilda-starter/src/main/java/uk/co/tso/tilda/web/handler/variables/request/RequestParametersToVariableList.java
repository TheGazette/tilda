package uk.co.tso.tilda.web.handler.variables.request;

import org.springframework.util.MultiValueMap;
import uk.co.tso.tilda.core.api.variable.Variable;
import uk.co.tso.tilda.core.config.ConfigurationContext;

import java.util.List;
import java.util.function.Function;


public interface RequestParametersToVariableList extends Function<MultiValueMap<String, String>, List<Variable>> {
    interface Factory {
        static RequestParametersToVariableList build(final ConfigurationContext config) {
            final var requestParameterToVariable =
                    RequestParameterToVariable.Factory.build(config);

            return (params) -> params.entrySet()
                    .stream()
                    .filter(e -> !e.getKey().startsWith("_"))
                    .filter(e -> !"callback".equalsIgnoreCase(e.getKey()))
                    .map(e -> requestParameterToVariable.apply(e.getKey(), e.getValue()))
                    .toList();
        }
    }
}
