package io.github.thegazette.tilda.core.processor.query.select.constructed.sorting.constructed;

import com.google.common.collect.ImmutableList;
import io.github.thegazette.tilda.core.processor.context.input.ItemsInputContext;
import io.github.thegazette.tilda.core.processor.query.select.constructed.SelectQueryGenerator;
import io.github.thegazette.tilda.core.processor.query.select.constructed.sorting.constructed.conditions.OrderCondition;
import io.github.thegazette.tilda.core.processor.query.select.constructed.sorting.constructed.conditions.OrderConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;
import io.github.thegazette.tilda.core.api.API;
import io.github.thegazette.tilda.core.api.Configuration;
import io.github.thegazette.tilda.core.api.Endpoint;
import io.github.thegazette.tilda.core.api.Selector;
import io.github.thegazette.tilda.core.api.vocabulary.Vocabulary;
import io.github.thegazette.tilda.core.exceptions.PropertyNotFoundException;

import java.util.Optional;
import java.util.function.BiConsumer;

public final class SelectorParentSort implements BiConsumer<ItemsInputContext, SelectQueryGenerator.QueryBuilder> {
    private final Configuration configuration;
    private final Vocabulary vocabulary;
    private final Logger logger = LoggerFactory.getLogger(SelectorParentSort.class);

    private SelectorParentSort(Configuration configuration) {
        this.configuration = configuration;
        this.vocabulary = configuration.vocabulary();
    }

    public static SelectorParentSort build(final Configuration configuration, final API api, final Endpoint endpoint) {
        return new SelectorParentSort(configuration);
    }
    @Override
    public void accept(ItemsInputContext context, SelectQueryGenerator.QueryBuilder builder) {
        if (builder.isRequestParameterOrderBySet() || builder.isRequestParameterSortSet() || builder.isSelectorOrderBySet() || builder.isSelectorSortSet())
            return;

        context
                .selector()
                .flatMap(Selector::parent)
                .flatMap(configuration::selector)
                .flatMap(this::sort)
                .ifPresent(sort -> {
                    final var seen = builder.alreadySeenParameterNames();
                    try {
                        var patterns = ImmutableList.<SelectQueryGenerator.QueryBuilder.SameSubject>builder();
                        var conditions = ImmutableList.<String>builder();

                        for (OrderCondition orderCondition : OrderConditions.of(sort)) {
                            if (!seen.contains(orderCondition.specification()))
                                patterns.add(orderCondition.toSameSubject(vocabulary));
                            conditions.add(orderCondition.condition());
                        }

                        builder.selectorSortPatterns(patterns.build());
                        builder.selectorSortConditions(conditions.build());
                    } catch (PropertyNotFoundException ex) {
                        throw new ResponseStatusException(HttpStatusCode.valueOf(500));
                    }
                });
    }

    private Optional<String> sort(Selector parent) {
        if (parent.sort().isPresent())
            return parent.sort();

        if(parent.select().isPresent())
            logger.warn("Attempting to get a sort condition from a parent selector that has an explicit query");

        final var grandParent = parent.parent().flatMap(configuration::selector);
        if (grandParent.isPresent())
            return sort(grandParent.get());

        return Optional.empty();
    }

}
