package uk.co.tso.tilda.core.processor.query.select.constructed.sorting.constructed.conditions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public interface OrderConditions {
    static List<OrderCondition> of(String spec) {
        if (spec == null || spec.isEmpty())
            return Collections.emptyList();

        final var values = spec.split(",");
        final Function<String, OrderCondition> mapper = value -> {
            if (value.startsWith("-")) {
                return new OrderCondition.Descending(value.replaceFirst("-", ""));
            }
            return new OrderCondition.Ascending(value);
        };

        return Arrays.stream(values).map(String::trim).map(mapper).toList();

    }
}
