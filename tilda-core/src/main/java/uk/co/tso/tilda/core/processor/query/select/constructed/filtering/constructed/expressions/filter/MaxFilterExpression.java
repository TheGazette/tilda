package uk.co.tso.tilda.core.processor.query.select.constructed.filtering.constructed.expressions.filter;

import uk.co.tso.tilda.core.api.vocabulary.Vocabulary;
import uk.co.tso.tilda.core.processor.query.select.constructed.filtering.constructed.parameter.Parameter;

public final class MaxFilterExpression extends BaseFilterExpression {
    private MaxFilterExpression(Parameter.Max max, Vocabulary vocabulary) {
        super(max, vocabulary, " <= ");
    }

    public static MaxFilterExpression from(Parameter.Max max, Vocabulary vocabulary) {
        return new MaxFilterExpression(max, vocabulary);
    }

}

