package uk.co.tso.tilda.core.processor.query.select.explicit;

import uk.co.tso.tilda.core.api.API;
import uk.co.tso.tilda.core.api.Configuration;
import uk.co.tso.tilda.core.api.Endpoint;
import uk.co.tso.tilda.core.api.Selector;
import uk.co.tso.tilda.core.processor.context.input.InputContext;
import uk.co.tso.tilda.core.processor.context.input.ItemsInputContext;
import uk.co.tso.tilda.core.processor.query.binding.SPARQLVariableBinder;

import java.util.Optional;
import java.util.function.Function;

public interface ExplicitSelectQuery extends Function<ItemsInputContext, Optional<String>> {
    static ExplicitSelectQuery build(final Configuration configuration, final API api, final Endpoint endpoint) {
        final var binder = SPARQLVariableBinder.build(configuration, api, endpoint);
        return (context) -> {
            final Function<String, String> bind = s -> binder.apply(context, s);

            if (context.param("_select").isPresent())
                return context.param("_select").map(bind);

            return context.selector().flatMap(Selector::select).map(bind);
        };
    }
}
