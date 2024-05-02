package io.github.thegazette.tilda.core.processor.query.binding;

import io.github.thegazette.tilda.core.api.API;
import io.github.thegazette.tilda.core.api.Configuration;
import io.github.thegazette.tilda.core.api.Endpoint;

import java.util.regex.Pattern;

/*
 * According to the spec:
 *
 *  variable bindings are used to replace SPARQL variable references within the pattern (any ?varName) with one of:
 *
 *  <varValue> if the variable is of type rdfs:Resource or
 *  "varValue"^^varType if the variable is of a specific datatype or
 *  "varValue"@varLang if the variable has a specific language or
 *  "varValue" if the variable is a plain literal
 *  Note that this may leave some variables within the SPARQL query; that's fine.
 *
 * This function implements the ability to bind variable values in _select, api:select, _where and api:where
 */
public interface SPARQLVariableBinder extends VariableBinder<String> {
    static SPARQLVariableBinder build(final Configuration configuration, final API api, final Endpoint endpoint) {
        return (context, sparql) -> {
            var bound = sparql;
            for (var variable : context.variables()) {
                final var pattern = Pattern.compile("\\W\\?"+ variable.name() +"\\b");
                final var value = variable.sparql();
                var matcher = pattern.matcher(bound);

                bound =  matcher.replaceAll(value);
            }
            return bound;
        };
    }
}
