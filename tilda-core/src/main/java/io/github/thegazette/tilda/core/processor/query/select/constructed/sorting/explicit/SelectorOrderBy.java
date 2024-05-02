package io.github.thegazette.tilda.core.processor.query.select.constructed.sorting.explicit;

import io.github.thegazette.tilda.core.processor.context.input.ItemsInputContext;
import io.github.thegazette.tilda.core.processor.query.select.constructed.SelectQueryGenerator;
import io.github.thegazette.tilda.core.api.API;
import io.github.thegazette.tilda.core.api.Configuration;
import io.github.thegazette.tilda.core.api.Endpoint;
import io.github.thegazette.tilda.core.api.Selector;

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
