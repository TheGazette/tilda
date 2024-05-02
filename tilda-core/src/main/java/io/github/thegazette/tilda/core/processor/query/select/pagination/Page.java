package io.github.thegazette.tilda.core.processor.query.select.pagination;

import io.github.thegazette.tilda.core.processor.context.input.InputContext;
import io.github.thegazette.tilda.core.api.API;
import io.github.thegazette.tilda.core.api.Configuration;
import io.github.thegazette.tilda.core.api.Endpoint;

import java.util.Optional;
import java.util.function.Function;

public interface Page extends Function<InputContext, Optional<Integer>> {
    static Page build(final Configuration configuration, final API api, final Endpoint endpoint) {
        final var pageSize = PageSize.build(configuration, api, endpoint);
        return (context) -> {
            final int limit = pageSize.apply(context);
            return context.param("_page").map(PaginationUtils::parse).map(i -> (i - 1) * limit);
        };
    }
}
