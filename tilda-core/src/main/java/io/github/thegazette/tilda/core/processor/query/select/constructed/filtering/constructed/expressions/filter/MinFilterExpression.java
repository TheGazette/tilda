package io.github.thegazette.tilda.core.processor.query.select.constructed.filtering.constructed.expressions.filter;

import io.github.thegazette.tilda.core.api.vocabulary.Vocabulary;
import io.github.thegazette.tilda.core.processor.query.select.constructed.filtering.constructed.parameter.Parameter;

public final class MinFilterExpression extends BaseFilterExpression {
    private MinFilterExpression(Parameter.Min min, Vocabulary vocabulary) {
        super(min, vocabulary, " >= ");

    }

    public static MinFilterExpression from(Parameter.Min min, final Vocabulary vocabulary) {
        return new MinFilterExpression(min, vocabulary);
    }

}
