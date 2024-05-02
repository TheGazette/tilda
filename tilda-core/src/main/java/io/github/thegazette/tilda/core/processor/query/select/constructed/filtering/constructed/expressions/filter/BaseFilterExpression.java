package io.github.thegazette.tilda.core.processor.query.select.constructed.filtering.constructed.expressions.filter;

import io.github.thegazette.tilda.core.api.vocabulary.Vocabulary;
import io.github.thegazette.tilda.core.processor.query.select.constructed.SelectQueryGenerator;
import io.github.thegazette.tilda.core.processor.query.select.constructed.filtering.constructed.expressions.FilterExpression;
import io.github.thegazette.tilda.core.processor.query.select.constructed.filtering.constructed.parameter.Parameter;

import java.util.Arrays;
import java.util.stream.Collectors;

public sealed abstract class BaseFilterExpression implements FilterExpression.WithSameSubject, FilterExpression.WithFilter permits
        MinFilterExpression, MinExFilterExpression, MaxFilterExpression, MaxExFilterExpression {

    private final Parameter parameter;
    private final SelectQueryGenerator.QueryBuilder.SameSubject sameSubject;
    private final SelectQueryGenerator.QueryBuilder.Filter filter;

    protected BaseFilterExpression(Parameter parameter, Vocabulary vocabulary, String expression) {
        this.parameter = parameter;

        final var labels = parameter.name().split("\\.");

        final var path = Arrays.stream(labels)
                .map(vocabulary::getPropertyIRIByLabel)
                .collect(Collectors.joining("/"));

        final var label = labels[labels.length - 1];
        final var variable = "?" + label;

        this.sameSubject = new SelectQueryGenerator.QueryBuilder.SameSubject(parameter.name(), path , variable);
        this.filter = new SelectQueryGenerator.QueryBuilder.Filter(parameter.name(), variable + parameter.value());
    }

    @Override
    public SelectQueryGenerator.QueryBuilder.SameSubject sameSubject() {
        return sameSubject;
    }

    @Override
    public SelectQueryGenerator.QueryBuilder.Filter filter() {
        return filter;
    }

    @Override
    public String name() {
        return parameter.name();
    }
}
