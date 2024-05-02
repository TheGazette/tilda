package io.github.thegazette.tilda.core.processor.query.select.pagination;

import io.github.thegazette.tilda.core.processor.context.input.InputContext;
import io.github.thegazette.tilda.core.api.API;
import io.github.thegazette.tilda.core.api.Configuration;
import io.github.thegazette.tilda.core.api.Endpoint;

import java.util.function.Function;

public interface PageSize extends Function<InputContext, Integer> {
    static PageSize build(final Configuration configuration, final API api, final Endpoint endpoint) {
        final Integer maxPageSize = api.maxPageSize().map(Integer::parseInt).orElse(0);
        final Function<Integer, Integer> ceiling = maxPageSize > 0
                                ? (i) -> (i > maxPageSize ? maxPageSize : i)
                                : Function.identity();

        final Integer defaultPageSize = endpoint
                                .defaultPageSize()
                                .map(Integer::parseInt)
                                .orElse(api.defaultPageSize().map(Integer::parseInt).orElse(10));



        return (context) -> context.param("_pageSize").map(PaginationUtils::parse).map(ceiling).orElse(defaultPageSize);
    }

}

