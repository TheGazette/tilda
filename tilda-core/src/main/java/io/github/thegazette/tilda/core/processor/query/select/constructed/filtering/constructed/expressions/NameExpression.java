package io.github.thegazette.tilda.core.processor.query.select.constructed.filtering.constructed.expressions;

import io.github.thegazette.tilda.core.api.vocabulary.Vocabulary;
import io.github.thegazette.tilda.core.processor.query.select.constructed.SelectQueryGenerator;
import io.github.thegazette.tilda.core.util.Constants;
import io.github.thegazette.tilda.core.processor.query.select.constructed.filtering.constructed.parameter.Parameter;

import java.util.Arrays;
import java.util.stream.Collectors;

public class NameExpression implements FilterExpression.WithSameSubject, FilterExpression.WithPattern{
    private final Parameter.Name name;
    private final SelectQueryGenerator.QueryBuilder.SameSubject sameSubject;
    private final SelectQueryGenerator.QueryBuilder.Pattern pattern;

    private NameExpression(Parameter.Name name, SelectQueryGenerator.QueryBuilder.SameSubject sameSubject, SelectQueryGenerator.QueryBuilder.Pattern pattern) {
        this.name = name;
        this.sameSubject = sameSubject;
        this.pattern = pattern;
    }

    public static NameExpression from(Parameter.Name name, Vocabulary vocabulary) {
        final var labels = name.name().split("\\.");

        final var path = Arrays.stream(labels)
                .map(vocabulary::getPropertyIRIByLabel)
                .collect(Collectors.joining("/"));

        final var label = labels[labels.length - 1];
        final var variable = "?" + label;

        var sameSubject = new SelectQueryGenerator.QueryBuilder.SameSubject(name.name(), path , variable);
        var pattern = new SelectQueryGenerator.QueryBuilder.Pattern(name.name(), name.value() + " " + Constants.RDFS.LABEL + " " + variable);

        return new NameExpression(name, sameSubject, pattern);
    }

    @Override
    public SelectQueryGenerator.QueryBuilder.SameSubject sameSubject() {
        return sameSubject;
    }

    @Override
    public SelectQueryGenerator.QueryBuilder.Pattern pattern() {
        return pattern;
    }

    @Override
    public String name() {
        return name.name();
    }
}
