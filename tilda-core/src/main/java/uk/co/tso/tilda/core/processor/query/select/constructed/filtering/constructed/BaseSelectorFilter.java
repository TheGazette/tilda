package uk.co.tso.tilda.core.processor.query.select.constructed.filtering.constructed;

import com.google.common.collect.ImmutableList;
import org.springframework.http.HttpStatusCode;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import uk.co.tso.tilda.core.api.vocabulary.Vocabulary;
import uk.co.tso.tilda.core.exceptions.PropertyNotFoundException;
import uk.co.tso.tilda.core.processor.context.input.InputContext;
import uk.co.tso.tilda.core.processor.query.select.constructed.filtering.constructed.expressions.FilterExpression;
import uk.co.tso.tilda.core.processor.query.select.constructed.filtering.constructed.parameter.Parameter;

import java.util.List;
import java.util.Set;

public abstract class BaseSelectorFilter extends BaseParameterFilter {

    protected List<FilterExpression> expressions(List<Parameter> parameters, Vocabulary vocabulary) {
        try {
            return parameters.stream().map(p -> FilterExpression.from(p, vocabulary)).toList();
        } catch (PropertyNotFoundException ex) {
            /*
             * According to the spec -
             *  It is an error if any of the parts cannot be mapped to a property.
             *  The API should return a 500 Internal Server Error if it was present in the configuration.
             */
            throw new ResponseStatusException(HttpStatusCode.valueOf(500));
        }

    }

    protected abstract Vocabulary vocabulary();

    /**
     * According to the spec:
     *
     * The api:filter property should be turned into a set of parameter bindings as if it were a URI query string.
     * Any values that follow the pattern {varName} should be replaced by the value of that variable.
     *
     * So here were take a processing context (to lookup variables) and a filter
     * and turn it into what we would get if it was a query string that has had values bound
     */
    protected MultiValueMap<String, String> asQueryParameters(final InputContext context, final String filter) {
        return UriComponentsBuilder.fromUriString("?" + filter)
                .uriVariables(context.variableMap())
                .build()
                .getQueryParams();
    }

    /**
     * Turn the output of asQueryParameters into a List of Parameter, filtering out any
     * that we have seen before
     */
    protected List<Parameter> parameters(final MultiValueMap<String, String> params, final Set<String> seen) {
        final var builder = ImmutableList.<Parameter>builder();

        params.forEach((k, vs) -> {
            if (k != null && !k.isEmpty() && !seen.contains(k)) {
                vs.forEach(v -> {
                    builder.add(Parameter.of(k, v));
                });
            }
        });

        return builder.build();

    }
}
