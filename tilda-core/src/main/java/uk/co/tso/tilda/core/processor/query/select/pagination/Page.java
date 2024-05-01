package uk.co.tso.tilda.core.processor.query.select.pagination;

import uk.co.tso.tilda.core.api.API;
import uk.co.tso.tilda.core.api.Configuration;
import uk.co.tso.tilda.core.api.Endpoint;
import uk.co.tso.tilda.core.processor.context.input.InputContext;

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
