package io.github.thegazette.tilda.core.processor.query.select.constructed.sorting.explicit;

import io.github.thegazette.tilda.core.processor.context.input.ItemsInputContext;
import io.github.thegazette.tilda.core.processor.query.select.constructed.SelectQueryGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.thegazette.tilda.core.api.API;
import io.github.thegazette.tilda.core.api.Configuration;
import io.github.thegazette.tilda.core.api.Endpoint;
import io.github.thegazette.tilda.core.api.Selector;

import java.util.Optional;
import java.util.function.BiConsumer;

public final class SelectorParentOrderBy implements BiConsumer<ItemsInputContext, SelectQueryGenerator.QueryBuilder> {
    private final Configuration configuration;
    private final Logger logger = LoggerFactory.getLogger(SelectorParentOrderBy.class);

    private SelectorParentOrderBy(Configuration configuration) {
        this.configuration = configuration;
    }

    public static SelectorParentOrderBy build(final Configuration configuration, final API api, final Endpoint endpoint) {
        return new SelectorParentOrderBy(configuration);
    }

    @Override
    public void accept(ItemsInputContext context, SelectQueryGenerator.QueryBuilder builder) {
        if (builder.isRequestParameterOrderBySet() || builder.isRequestParameterSortSet() || builder.isSelectorOrderBySet() || builder.isSelectorSortSet())
            return;

        context.selector()
                .flatMap(Selector::parent)
                .flatMap(configuration::selector)
                .flatMap(this::orderBy)
                .ifPresent(builder::selectorOrderBy);

    }

    private Optional<String> orderBy(Selector parent) {
        if (parent.orderBy().isPresent())
            return parent.orderBy();

        if(parent.select().isPresent())
            logger.warn("Attempting to get an orderBy clause from a parent selector that has an explicit query");

        final var grandParent = parent.parent().flatMap(configuration::selector);
        if (grandParent.isPresent())
            return orderBy(grandParent.get());

        return Optional.empty();
    }
}
