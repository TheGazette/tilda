package io.github.thegazette.tilda.core.processor.query.select.constructed.filtering.constructed.expressions.filter;

import io.github.thegazette.tilda.core.api.vocabulary.Vocabulary;
import io.github.thegazette.tilda.core.processor.query.select.constructed.filtering.constructed.parameter.Parameter;

public final class MaxExFilterExpression extends BaseFilterExpression {
    private MaxExFilterExpression(Parameter.MaxEx maxEx, Vocabulary vocabulary) {
        super(maxEx, vocabulary, " < " );
    }

    public static MaxExFilterExpression from(Parameter.MaxEx maxEx, Vocabulary vocabulary) {
        return new MaxExFilterExpression(maxEx, vocabulary);
    }

}

