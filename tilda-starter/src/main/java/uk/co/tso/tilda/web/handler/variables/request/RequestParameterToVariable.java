package uk.co.tso.tilda.web.handler.variables.request;

import uk.co.tso.tilda.core.api.variable.Variable;
import uk.co.tso.tilda.core.api.variable.request.ParamaterVariable;
import uk.co.tso.tilda.core.config.ConfigurationContext;

import java.util.List;
import java.util.function.BiFunction;

public interface RequestParameterToVariable extends BiFunction<String, List<String>, Variable> {

    interface Factory {
        static RequestParameterToVariable build(final ConfigurationContext config) {
            return (name, values) -> {
                if (name.toLowerCase().startsWith("lang-")) {
                    var pn = name.split("-");
                    return ParamaterVariable.from(name, pn[1]);
                }
                var property = config.configuration().vocabulary().findPropertyByLabel(name).orElse(null);
                return ParamaterVariable.from(name, String.join(",", values), property);
            };
        }
    }

}
