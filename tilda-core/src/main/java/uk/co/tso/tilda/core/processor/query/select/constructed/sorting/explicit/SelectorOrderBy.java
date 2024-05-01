package uk.co.tso.tilda.core.processor.query.select.constructed.sorting.explicit;

import uk.co.tso.tilda.core.api.API;
import uk.co.tso.tilda.core.api.Configuration;
import uk.co.tso.tilda.core.api.Endpoint;
import uk.co.tso.tilda.core.api.Selector;
import uk.co.tso.tilda.core.processor.context.input.InputContext;
import uk.co.tso.tilda.core.processor.context.input.ItemsInputContext;
import uk.co.tso.tilda.core.processor.query.select.constructed.SelectQueryGenerator;

import java.util.function.BiConsumer;

public interface SelectorOrderBy extends BiConsumer<ItemsInputContext, SelectQueryGenerator.QueryBuilder> {
    static SelectorOrderBy build(final Configuration configuration, final API api, final Endpoint endpoint) {
        return (context, builder) -> {
            if (builder.isRequestParameterOrderBySet() || builder.isRequestParameterSortSet())
                return;

            context.selector().flatMap(Selector::orderBy).ifPresent(builder::selectorOrderBy);
        };
    }
}
