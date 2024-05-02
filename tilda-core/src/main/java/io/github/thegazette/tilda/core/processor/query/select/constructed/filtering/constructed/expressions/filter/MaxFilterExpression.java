package io.github.thegazette.tilda.core.processor.query.select.constructed.filtering.constructed.expressions.filter;

import io.github.thegazette.tilda.core.api.vocabulary.Vocabulary;
import io.github.thegazette.tilda.core.processor.query.select.constructed.filtering.constructed.parameter.Parameter;

public final class MaxFilterExpression extends BaseFilterExpression {
    private MaxFilterExpression(Parameter.Max max, Vocabulary vocabulary) {
        super(max, vocabulary, " <= ");
    }

    public static MaxFilterExpression from(Parameter.Max max, Vocabulary vocabulary) {
        return new MaxFilterExpression(max, vocabulary);
    }

}

