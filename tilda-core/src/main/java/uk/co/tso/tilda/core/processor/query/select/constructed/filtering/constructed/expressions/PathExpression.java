package uk.co.tso.tilda.core.processor.query.select.constructed.filtering.constructed.expressions;

import uk.co.tso.tilda.core.api.vocabulary.Vocabulary;
import uk.co.tso.tilda.core.exceptions.PropertyNotFoundException;
import uk.co.tso.tilda.core.processor.query.select.constructed.SelectQueryGenerator;
import uk.co.tso.tilda.core.processor.query.select.constructed.filtering.constructed.parameter.Parameter;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class PathExpression implements FilterExpression.WithSameSubject {
    private final Parameter.Plain parameter;
    private final SelectQueryGenerator.QueryBuilder.SameSubject sameSubject;

    private PathExpression(Parameter.Plain parameter, SelectQueryGenerator.QueryBuilder.SameSubject sameSubject) {
        this.parameter = parameter;
        this.sameSubject = sameSubject;
    }

    public static PathExpression from(final Parameter.Plain parameter, final Vocabulary vocabulary) {
        final var labels = parameter.name().split("\\.");

        final var path = Arrays.stream(labels)
                .map(vocabulary::getPropertyIRIByLabel)
                .collect(Collectors.joining("/"));

        var valProp = vocabulary.findPropertyByLabel(labels[labels.length - 1]).orElseThrow(PropertyNotFoundException::new);

        var sameSubject =  new SelectQueryGenerator.QueryBuilder.SameSubject(parameter.name(), path , FilterExpression.queryableValue(vocabulary, parameter, valProp));
        return new PathExpression(parameter, sameSubject);
    }


    @Override
    public String name() {
        return parameter.name();
    }

    @Override
    public SelectQueryGenerator.QueryBuilder.SameSubject sameSubject() {
        return sameSubject;
    }


}
