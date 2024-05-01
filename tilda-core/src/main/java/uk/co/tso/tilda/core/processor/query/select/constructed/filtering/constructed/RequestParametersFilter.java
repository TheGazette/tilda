package uk.co.tso.tilda.core.processor.query.select.constructed.filtering.constructed;

import com.google.common.collect.ImmutableList;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;
import uk.co.tso.tilda.core.api.API;
import uk.co.tso.tilda.core.api.Configuration;
import uk.co.tso.tilda.core.api.Endpoint;
import uk.co.tso.tilda.core.api.vocabulary.Vocabulary;
import uk.co.tso.tilda.core.exceptions.PropertyNotFoundException;
import uk.co.tso.tilda.core.processor.context.input.InputContext;
import uk.co.tso.tilda.core.processor.query.select.constructed.SelectQueryGenerator;
import uk.co.tso.tilda.core.processor.query.select.constructed.filtering.constructed.expressions.FilterExpression;
import uk.co.tso.tilda.core.processor.query.select.constructed.filtering.constructed.parameter.Parameter;

import java.util.List;
import java.util.function.BiConsumer;

/*
 * According to the spec, we add the unreserved request parameters to the query
 * (even though we're also using these for variable binding)
 *
 * We need to reify them to known properties, which includes
 * (according to spec) "splitting the name on the dots"
 *
 * It's not entirely clear to me what splitting the name
 * on the dots is meant to produce (all the spec says is "splitting it on the dots").
 * https://github.com/UKGovLD/linked-data-api/blob/wiki/API_Selecting_Resources.md
 *
 * Looking at the section on "Query Parameters"
 * https://github.com/UKGovLD/linked-data-api/blob/wiki/API_Query_Parameters.md
 * it says "In each of these cases, param may be a property path as described above."
 *
 * And if we go up to the section on Property Paths
 * https://github.com/UKGovLD/linked-data-api/blob/wiki/API_Property_Paths.md
 * All it says is "property paths can be used to point to values within the data"
 * followed by some Backus–Naur like syntax. That's it. The rest of the page basically
 * describes reifying these short property names to the "real thing". It doesn't actually
 * tell you how property paths can actually be used to point to values in the data.
 *
 * The viewer section specifically mentions "property chains", not property paths
 * https://github.com/UKGovLD/linked-data-api/blob/wiki/API_Viewing_Resources.md
 * So I don't we're talking about the same thing, even though they look very
 * similar.
 *
 * SPARQL has "Property Paths" but with a different syntax ("ex:Prop1/ex:Prop2")
 * where "?item ex:Prop1/ex:Prop2 ?var" is a short form for
 *   ?item ex:Prop1 ?temp1
 *   ?temp1 ex:Prop2 ?var
 *
 * So I'm wondering if this is what is meant by the spec. Though you would
 * think they would have linked to the SPARQL docs if that was the case -
 * they do this for other concepts, why not this one?
 *
 * Unless and until someone objects, I'm going with the SPARQL concept
 * of Property Paths, so x.y=foo would become
 *   ?item <https://example.com/ns#x>/<https://example.com/ns#y> foo .
 *
 */
public class RequestParametersFilter extends BaseParameterFilter implements BiConsumer<InputContext, SelectQueryGenerator.QueryBuilder> {
    private final Vocabulary vocabulary;
    private RequestParametersFilter(Configuration configuration) {
        this.vocabulary = configuration.vocabulary();
    }

    public static RequestParametersFilter build(final Configuration configuration, final API api, final Endpoint endpoint) {
        return new RequestParametersFilter(configuration);
    }

    @Override
    public void accept(InputContext context, SelectQueryGenerator.QueryBuilder queryBuilder) {
        /*
         * We'll start by converting the request params multimap into a list a Parameter
         * (to help deal with removing the prefixes)
         */
        final var parameters = parameters(context);

        if (parameters.isEmpty())
            return;

        var expressions = expressions(parameters);

        var sameSubjects = sameSubjects(expressions);
        var patterns = patterns(expressions);
        var filters = filters(expressions);

        queryBuilder.requestParameterSameSubjects(sameSubjects);
        queryBuilder.requestParameterPatterns(patterns);
        queryBuilder.requestParameterFilters(filters);
    }

    private List<Parameter> parameters(final InputContext context) {
        final var builder = ImmutableList.<Parameter>builder();

        context.request().params().forEach((k, vs) -> {
            if (k != null && !k.isEmpty() && !k.startsWith("_") && !"callback".equalsIgnoreCase(k)) {
                vs.forEach(v -> {
                    builder.add(Parameter.of(k, v));
                });
            }
        });

        return builder.build();

    }

    private List<FilterExpression> expressions(final List<Parameter> parameters) {
        try {
            return parameters.stream().map(p -> FilterExpression.from(p, vocabulary)).toList();
        } catch (PropertyNotFoundException ex) {
            /*
             * According to the spec -
             *  It is an error if any of the parts cannot be mapped to a property.
             *  The API should return a 400 Bad Request error if the parameter binding came from a request parameter
             */
            throw new ResponseStatusException(HttpStatusCode.valueOf(400));
        }
    }

}
