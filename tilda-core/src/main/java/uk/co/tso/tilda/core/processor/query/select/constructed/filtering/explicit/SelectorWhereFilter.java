package uk.co.tso.tilda.core.processor.query.select.constructed.filtering.explicit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.tso.tilda.core.api.API;
import uk.co.tso.tilda.core.api.Configuration;
import uk.co.tso.tilda.core.api.Endpoint;
import uk.co.tso.tilda.core.api.Selector;
import uk.co.tso.tilda.core.processor.context.input.InputContext;
import uk.co.tso.tilda.core.processor.context.input.ItemsInputContext;
import uk.co.tso.tilda.core.processor.query.binding.SPARQLVariableBinder;
import uk.co.tso.tilda.core.processor.query.select.constructed.SelectQueryGenerator;
import uk.co.tso.tilda.core.util.Optionals;

import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * Returns the where clause defined on the resolved selector
 *
 * TODO - Add variable binding
 * TODO - Is adding where clauses from parent selectors correct? The spec is kind of ambiguous
 */
public class SelectorWhereFilter implements BiConsumer<ItemsInputContext, SelectQueryGenerator.QueryBuilder> {
    private final Configuration configuration;
    private final SPARQLVariableBinder binder;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private SelectorWhereFilter(Configuration configuration, final API api, final Endpoint endpoint) {
        this.configuration = configuration;
        this.binder = SPARQLVariableBinder.build(configuration, api, endpoint);
    }

    public static SelectorWhereFilter build(final Configuration configuration, final API api, final Endpoint endpoint) {
        return new SelectorWhereFilter(configuration, api, endpoint);
    }

    @Override
    public void accept(ItemsInputContext context, SelectQueryGenerator.QueryBuilder queryBuilder) {
        queryBuilder.selectorWhere(apply(context).orElse(""));
    }

    private Optional<String> apply(ItemsInputContext context) {
        return context.selector()
                .flatMap(selector -> filter(context, selector))
                .map(w -> binder.apply(context, w));
    }

    private Optional<String> filter(InputContext context, Selector selector) {
        var filter = selector.where().orElse("");
        var parent = selector
                .parent()
                .flatMap(configuration::selector);

        parent.ifPresent(p -> {
            p.select().ifPresent(select -> {
                logger.warn("Selector {} is defined as a parent selector of {} but uses an explicit select statement", p.name(), selector.name());
            });
        });

        var pfilter = parent.flatMap(p -> filter(context, p)).orElse("");

        return Optionals.ofString((filter + "\r\n" + pfilter).trim());
    }
}
