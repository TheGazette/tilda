package io.github.thegazette.tilda.core.processor.query.select.constructed.sorting.explicit;

import io.github.thegazette.tilda.core.processor.context.input.InputContext;
import io.github.thegazette.tilda.core.processor.query.select.constructed.SelectQueryGenerator;
import io.github.thegazette.tilda.core.api.API;
import io.github.thegazette.tilda.core.api.Configuration;
import io.github.thegazette.tilda.core.api.Endpoint;

import java.util.function.BiConsumer;

public interface RequestParameterOrderBy extends BiConsumer<InputContext, SelectQueryGenerator.QueryBuilder> {
    static RequestParameterOrderBy build(final Configuration configuration, final API api, final Endpoint endpoint) {
        return (context, builder) -> builder.requestParameterOrderBy(context.param("_orderBy").orElse(""));
    }

}
