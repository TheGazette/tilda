package uk.co.tso.tilda.core.processor.query.select.constructed.sorting.constructed;

import com.google.common.collect.ImmutableList;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;
import uk.co.tso.tilda.core.api.API;
import uk.co.tso.tilda.core.api.Configuration;
import uk.co.tso.tilda.core.api.Endpoint;
import uk.co.tso.tilda.core.exceptions.PropertyNotFoundException;
import uk.co.tso.tilda.core.processor.context.input.InputContext;
import uk.co.tso.tilda.core.processor.query.select.constructed.SelectQueryGenerator;
import uk.co.tso.tilda.core.processor.query.select.constructed.SelectQueryGenerator.QueryBuilder.SameSubject;
import uk.co.tso.tilda.core.processor.query.select.constructed.sorting.constructed.conditions.OrderCondition;
import uk.co.tso.tilda.core.processor.query.select.constructed.sorting.constructed.conditions.OrderConditions;

import java.util.function.BiConsumer;

public interface RequestParameterSort extends BiConsumer<InputContext, SelectQueryGenerator.QueryBuilder> {
    static RequestParameterSort build(final Configuration configuration, final API api, final Endpoint endpoint) {
        final var vocabulary = configuration.vocabulary();
        return (context, builder) -> {
            if (builder.isRequestParameterOrderBySet())
                return;

            context.param("_sort").ifPresent(sort -> {
                final var seen = builder.alreadySeenParameterNames();
                try {
                    var patterns = ImmutableList.<SameSubject>builder();
                    var conditions = ImmutableList.<String>builder();

                    for (OrderCondition orderCondition : OrderConditions.of(sort)) {
                        if (!seen.contains(orderCondition.specification()))
                            patterns.add(orderCondition.toSameSubject(vocabulary));
                        conditions.add(orderCondition.condition());
                    }

                    builder.requestParameterSortPatterns(patterns.build());
                    builder.requestParameterSortConditions(conditions.build());
                } catch (PropertyNotFoundException ex) {
                    throw new ResponseStatusException(HttpStatusCode.valueOf(400));
                }
            });
        };
    }
}
