package uk.co.tso.tilda.core.processor.query.select.constructed.filtering.constructed.expressions.filter;

import uk.co.tso.tilda.core.api.vocabulary.Vocabulary;
import uk.co.tso.tilda.core.processor.query.select.constructed.filtering.constructed.parameter.Parameter;

public final class MinFilterExpression extends BaseFilterExpression {
    private MinFilterExpression(Parameter.Min min, Vocabulary vocabulary) {
        super(min, vocabulary, " >= ");

    }

    public static MinFilterExpression from(Parameter.Min min, final Vocabulary vocabulary) {
        return new MinFilterExpression(min, vocabulary);
    }

}
