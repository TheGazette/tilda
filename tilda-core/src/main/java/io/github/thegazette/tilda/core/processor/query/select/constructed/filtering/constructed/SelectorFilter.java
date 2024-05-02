package io.github.thegazette.tilda.core.processor.query.select.constructed.filtering.constructed;

import io.github.thegazette.tilda.core.processor.context.input.ItemsInputContext;
import io.github.thegazette.tilda.core.api.API;
import io.github.thegazette.tilda.core.api.Configuration;
import io.github.thegazette.tilda.core.api.Endpoint;
import io.github.thegazette.tilda.core.api.Selector;
import io.github.thegazette.tilda.core.api.vocabulary.Vocabulary;
import io.github.thegazette.tilda.core.processor.query.select.constructed.SelectQueryGenerator;

import java.util.function.BiConsumer;

public class SelectorFilter extends BaseSelectorFilter implements BiConsumer<ItemsInputContext, SelectQueryGenerator.QueryBuilder> {
    private final Configuration configuration;
    private final Vocabulary vocabulary;

    private SelectorFilter(Configuration configuration) {
        this.configuration = configuration;
        this.vocabulary = configuration.vocabulary();
    }

    public static SelectorFilter build(final Configuration configuration, final API api, final Endpoint endpoint) {
        return new SelectorFilter(configuration);
    }

    @Override
    public void accept(ItemsInputContext context, SelectQueryGenerator.QueryBuilder queryBuilder) {
        var selector = context.selector();
        if (selector.isEmpty() || selector.flatMap(Selector::filter).isEmpty())
            return;

        var filter = selector.flatMap(Selector::filter).get();
        var seen = queryBuilder.alreadySeenParameterNames();

        var parameters = parameters(asQueryParameters(context, filter), seen);
        var expressions = expressions(parameters, vocabulary);

        var sameSubjects = sameSubjects(expressions);
        var patterns = patterns(expressions);
        var filters = filters(expressions);

        queryBuilder.selectorSameSubjects(sameSubjects);
        queryBuilder.selectorPatterns(patterns);
        queryBuilder.selectorFilters(filters);
    }

    @Override
    protected Vocabulary vocabulary() {
        return configuration.vocabulary();
    }
}
