package uk.co.tso.tilda.core.processor.query.select.constructed.filtering.constructed;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import uk.co.tso.tilda.core.api.API;
import uk.co.tso.tilda.core.api.Configuration;
import uk.co.tso.tilda.core.api.Endpoint;
import uk.co.tso.tilda.core.api.Selector;
import uk.co.tso.tilda.core.api.vocabulary.Vocabulary;
import uk.co.tso.tilda.core.processor.context.input.InputContext;
import uk.co.tso.tilda.core.processor.context.input.ItemsInputContext;
import uk.co.tso.tilda.core.processor.query.select.constructed.SelectQueryGenerator;
import uk.co.tso.tilda.core.processor.query.select.constructed.filtering.constructed.expressions.FilterExpression;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public class SelectorParentFilter extends BaseSelectorFilter implements BiConsumer<ItemsInputContext, SelectQueryGenerator.QueryBuilder> {
    private final Configuration configuration;
    private final Vocabulary vocabulary;

    private SelectorParentFilter(Configuration configuration) {
        this.configuration = configuration;
        this.vocabulary = vocabulary();
    }

    public static SelectorParentFilter build(final Configuration configuration, final API api, final Endpoint endpoint) {
        return new SelectorParentFilter(configuration);
    }

    @Override
    public void accept(ItemsInputContext context, SelectQueryGenerator.QueryBuilder builder) {
        var selector = context.selector();
        if (selector.isEmpty()) return;

        var parent = selector.flatMap(Selector::parent).flatMap(configuration::selector);
        if (parent.isEmpty()) return;

        var startingSeen = builder.alreadySeenParameterNames();
        var filterables = filterables(context, builder, parent.get(), startingSeen);
        var sameSubjects = sameSubjects(filterables);
        var filters = filters(filterables);


        builder.selectorParentSameSubjects(sameSubjects);
        builder.selectorParentFilters(filters);
    }


    private List<FilterExpression> filterables(InputContext context, SelectQueryGenerator.QueryBuilder builder, Selector selector, Set<String> seen) {
        var filterBuilder = ImmutableList.<FilterExpression>builder();
        selector.filter().ifPresent(filter -> {
            var parameters = parameters(asQueryParameters(context, filter), seen);
            parameters.stream().map(p -> FilterExpression.from(p, vocabulary)).forEach(filterBuilder::add);
        });
        var f = filterBuilder.build();
        var parentFilterBuilder = ImmutableList.<FilterExpression>builder();
        selector.parent().flatMap(configuration::selector).ifPresent(parent -> {
            var nowseen = ImmutableSet.<String>builder().addAll(seen).addAll(f.stream().map(FilterExpression::name).toList()).build();
            filterables(context, builder, parent, nowseen).forEach(parentFilterBuilder::add);
        });
        return ImmutableList.<FilterExpression>builder().build();
    }

    @Override
    protected Vocabulary vocabulary() {
        return configuration.vocabulary();
    }
}
