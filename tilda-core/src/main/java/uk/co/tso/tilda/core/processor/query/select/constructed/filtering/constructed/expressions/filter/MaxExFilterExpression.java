package uk.co.tso.tilda.core.processor.query.select.constructed.filtering.constructed.expressions.filter;

import uk.co.tso.tilda.core.api.vocabulary.Vocabulary;
import uk.co.tso.tilda.core.processor.query.select.constructed.filtering.constructed.parameter.Parameter;

public final class MaxExFilterExpression extends BaseFilterExpression {
    private MaxExFilterExpression(Parameter.MaxEx maxEx, Vocabulary vocabulary) {
        super(maxEx, vocabulary, " < " );
    }

    public static MaxExFilterExpression from(Parameter.MaxEx maxEx, Vocabulary vocabulary) {
        return new MaxExFilterExpression(maxEx, vocabulary);
    }

}

