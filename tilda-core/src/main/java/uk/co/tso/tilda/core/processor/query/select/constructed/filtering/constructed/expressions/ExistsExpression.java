package uk.co.tso.tilda.core.processor.query.select.constructed.filtering.constructed.expressions;

import uk.co.tso.tilda.core.api.vocabulary.Vocabulary;
import uk.co.tso.tilda.core.processor.query.select.constructed.SelectQueryGenerator;
import uk.co.tso.tilda.core.processor.query.select.constructed.filtering.constructed.parameter.Parameter;

import java.util.Arrays;
import java.util.stream.Collectors;

public sealed interface ExistsExpression extends FilterExpression permits ExistsExpression.True, ExistsExpression.False {

    static ExistsExpression from(Parameter.Exists exists, Vocabulary vocabulary) {
        return switch(exists.value().trim().toLowerCase()) {
            case "true" -> True.from(exists, vocabulary);
            case "false" -> False.from(exists, vocabulary);
            default -> throw new RuntimeException();
        };
    }

    final class True implements ExistsExpression, FilterExpression.WithSameSubject {
        private final Parameter.Exists exists;
        private final SelectQueryGenerator.QueryBuilder.SameSubject sameSubject;

        private True(Parameter.Exists exists, SelectQueryGenerator.QueryBuilder.SameSubject sameSubject) {
            this.exists = exists;
            this.sameSubject = sameSubject;
        }

        static True from(Parameter.Exists exists, Vocabulary vocabulary) {
            final var labels = exists.name().split("\\.");

            final var path = Arrays.stream(labels)
                    .map(vocabulary::getPropertyIRIByLabel)
                    .collect(Collectors.joining("/"));

            final var label = labels[labels.length - 1];
            final var variable = "?" + label;

            final var sameSubject = new SelectQueryGenerator.QueryBuilder.SameSubject(exists.name(), path , variable);
            return new True(exists, sameSubject);
        }
        @Override
        public SelectQueryGenerator.QueryBuilder.SameSubject sameSubject() {
            return sameSubject;
        }

        @Override
        public String name() {
            return exists.name();
        }
    }

    final class False implements ExistsExpression, FilterExpression.WithPattern, FilterExpression.WithFilter {
        private final Parameter.Exists exists;
        private final SelectQueryGenerator.QueryBuilder.Pattern pattern;
        private final SelectQueryGenerator.QueryBuilder.Filter filter;

        private False(Parameter.Exists exists, SelectQueryGenerator.QueryBuilder.Pattern pattern, SelectQueryGenerator.QueryBuilder.Filter filter) {
            this.exists = exists;
            this.pattern = pattern;
            this.filter = filter;
        }

        static False from(final Parameter.Exists exists, final Vocabulary vocabulary) {
            final var labels = exists.name().split("\\.");

            final var path = Arrays.stream(labels)
                    .map(vocabulary::getPropertyIRIByLabel)
                    .collect(Collectors.joining("/"));

            final var label = labels[labels.length - 1];
            final var variable = "?" + label;

            final var pattern = new SelectQueryGenerator.QueryBuilder.Pattern(exists.name(), "OPTIONAL { ?item " + path + " " + variable + "}");
            final var filter = new SelectQueryGenerator.QueryBuilder.Filter(exists.name(),  "!bound(" + variable + ")");

            return new False(exists, pattern, filter);
        }

        @Override
        public SelectQueryGenerator.QueryBuilder.Pattern pattern() {
            return pattern;
        }

        @Override
        public String name() {
            return exists.name();
        }

        @Override
        public SelectQueryGenerator.QueryBuilder.Filter filter() {
            return filter;
        }

    }
}
