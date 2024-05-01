package uk.co.tso.tilda.core.processor.query.select.constructed.sorting.explicit;

import uk.co.tso.tilda.core.api.API;
import uk.co.tso.tilda.core.api.Configuration;
import uk.co.tso.tilda.core.api.Endpoint;
import uk.co.tso.tilda.core.processor.context.input.InputContext;
import uk.co.tso.tilda.core.processor.query.select.constructed.SelectQueryGenerator;

import java.util.function.BiConsumer;

public interface RequestParameterOrderBy extends BiConsumer<InputContext, SelectQueryGenerator.QueryBuilder> {
    static RequestParameterOrderBy build(final Configuration configuration, final API api, final Endpoint endpoint) {
        return (context, builder) -> builder.requestParameterOrderBy(context.param("_orderBy").orElse(""));
    }

}
