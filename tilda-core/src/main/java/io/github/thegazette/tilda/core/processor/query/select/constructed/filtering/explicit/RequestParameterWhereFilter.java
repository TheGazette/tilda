package io.github.thegazette.tilda.core.processor.query.select.constructed.filtering.explicit;

import io.github.thegazette.tilda.core.processor.context.input.InputContext;
import io.github.thegazette.tilda.core.processor.query.binding.SPARQLVariableBinder;
import io.github.thegazette.tilda.core.api.API;
import io.github.thegazette.tilda.core.api.Configuration;
import io.github.thegazette.tilda.core.api.Endpoint;
import io.github.thegazette.tilda.core.processor.query.select.constructed.SelectQueryGenerator;

import java.util.function.BiConsumer;

/**
 * According to the spec, _where is a "GroupGraphPattern"
 * So I'm assuming it would be in the form %3Fitem%20param%20value
 *
 * TODO - check if we need to urldecode the value
 */
public interface RequestParameterWhereFilter extends BiConsumer<InputContext, SelectQueryGenerator.QueryBuilder> {
    static RequestParameterWhereFilter build(final Configuration configuration, final API api, final Endpoint endpoint) {
        final var binder = SPARQLVariableBinder.build(configuration, api, endpoint);

        return (context, builder) -> {
            final var clause =
                    context.param("_where")
                    .map(w -> binder.apply(context, w))
                    .orElse("");

            builder.requestParameterWhere(clause);
        };
    }
}
