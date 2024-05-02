package io.github.thegazette.tilda.core.processor.query.select;

import io.github.thegazette.tilda.core.processor.context.input.ItemsInputContext;
import io.github.thegazette.tilda.core.processor.query.select.constructed.SelectQueryGenerator;
import io.github.thegazette.tilda.core.processor.query.select.explicit.ExplicitSelectQuery;
import io.github.thegazette.tilda.core.processor.query.select.pagination.Page;
import io.github.thegazette.tilda.core.processor.query.select.pagination.PageSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.thegazette.tilda.core.api.API;
import io.github.thegazette.tilda.core.api.Configuration;
import io.github.thegazette.tilda.core.api.Endpoint;

import java.util.function.Function;

/**
 * Generate a Select Query
 *
 * TODO - Work out how to add variable binding to _select, api:select, _where and api:where
 * TODO - Find out if the assumption made about property paths is correct
 *
 */
public class SelectQuery implements Function<ItemsInputContext, SelectQuery.SelectQueryContext> {
    public record SelectQueryContext(String query, int startIndex, int itemsPerPage){}

    private static final Logger logger = LoggerFactory.getLogger(SelectQuery.class);
    private final ExplicitSelectQuery explicit;
    private final SelectQueryGenerator constructed;
    private final PageSize pageSize;
    private final Page page;


    private SelectQuery(final ExplicitSelectQuery explicit, final SelectQueryGenerator selectQueryGenerator,
                        final PageSize pageSize, final Page page) {
        this.explicit = explicit;
        this.constructed = selectQueryGenerator;
        this.pageSize = pageSize;
        this.page = page;
    }

    public static SelectQuery build(final Configuration configuration, final API api, final Endpoint endpoint) {
        final var explicit = ExplicitSelectQuery.build(configuration, api, endpoint);
        final var constructed = SelectQueryGenerator.build(configuration, api, endpoint);
        final var pageSize = PageSize.build(configuration, api, endpoint);
        final var page = Page.build(configuration, api, endpoint);
        return new SelectQuery(explicit, constructed, pageSize, page);
    }

    @Override
    public SelectQueryContext apply(ItemsInputContext context) {
        //TODO - I guess we should add prefixes?

        final var select = explicit.apply(context).orElse(constructed.apply(context));
        final var limit = pageSize.apply(context);
        final var startIndex = page.apply(context);
        final var offset = startIndex.map(i -> "OFFSET " + i).orElse("");

        final var query = new StringBuilder(select)
                .append("\r\nLIMIT ")
                .append(limit)
                .append("\r\n")
                .append(offset)
                .toString();

        return new SelectQueryContext(query, startIndex.orElse(0), limit);
    }



}
