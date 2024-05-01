package uk.co.tso.tilda.web.handler.variables.path;

import uk.co.tso.tilda.core.api.variable.Variable;
import uk.co.tso.tilda.core.api.variable.request.PathVariable;
import uk.co.tso.tilda.core.config.ConfigurationContext;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface PathVariablesToVariableList extends Function<Map<String, String>, List<Variable>> {
    interface Factory {
        static PathVariablesToVariableList build(final ConfigurationContext config) {

            final BiFunction<String, String, Variable> pathVariable = (key, value) -> {
                var property = config.configuration().vocabulary().findPropertyByLabel(key).orElse(null);
                return PathVariable.from(key, value, property);
            };

            final Function<Map.Entry<String, String>, Variable> toPathVariable =
                    (e) -> pathVariable.apply(e.getKey(), e.getValue());

            return (pathVars) -> pathVars
                    .entrySet()
                    .stream()
                    .map(toPathVariable)
                    .toList();
        }
    }

}
