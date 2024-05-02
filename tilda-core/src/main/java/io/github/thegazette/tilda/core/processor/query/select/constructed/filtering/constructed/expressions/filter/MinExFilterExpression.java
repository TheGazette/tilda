package io.github.thegazette.tilda.core.processor.query.select.constructed.filtering.constructed.expressions.filter;

import io.github.thegazette.tilda.core.api.vocabulary.Vocabulary;
import io.github.thegazette.tilda.core.processor.query.select.constructed.filtering.constructed.parameter.Parameter;

public final class MinExFilterExpression extends BaseFilterExpression {
    private MinExFilterExpression(Parameter parameter, Vocabulary vocabulary) {
        super(parameter, vocabulary, " > ");
    }

    public static MinExFilterExpression from(final Parameter.MinEx minEx, final Vocabulary vocabulary) {
        return new MinExFilterExpression(minEx, vocabulary);
    }

}
