package uk.co.tso.tilda.core.processor.query.select.constructed.sorting.constructed.conditions;

import uk.co.tso.tilda.core.api.vocabulary.Vocabulary;
import uk.co.tso.tilda.core.processor.query.select.constructed.SelectQueryGenerator;

import java.util.Arrays;
import java.util.stream.Collectors;

public sealed interface OrderCondition permits OrderCondition.Ascending, OrderCondition.Descending {

    record Ascending(String specification) implements OrderCondition{
        @Override
        public String condition() {
            final var labels = specification().split("\\.");
            final var variable = "?" + labels[labels.length - 1];
            return variable;
        }
    }
    record Descending(String specification) implements OrderCondition{
        @Override
        public String condition() {
            final var labels = specification().split("\\.");
            final var variable = "?" + labels[labels.length - 1];
            return "DESC(" + variable + ")";
        }
    }



    String specification();
    String condition();

    default SelectQueryGenerator.QueryBuilder.SameSubject toSameSubject(Vocabulary vocabulary) {
        final var labels = specification().split("\\.");

        final var path = Arrays.stream(labels)
                .map(vocabulary::getPropertyIRIByLabel)
                .collect(Collectors.joining("/"));

        var variable = "?" + labels[labels.length - 1];

        return new SelectQueryGenerator.QueryBuilder.SameSubject(specification(), path , variable);
    }


}
