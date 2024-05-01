package uk.co.tso.tilda.core.processor.query.select.constructed.filtering.constructed.expressions.filter;

import uk.co.tso.tilda.core.api.vocabulary.Vocabulary;
import uk.co.tso.tilda.core.processor.query.select.constructed.filtering.constructed.parameter.Parameter;

public final class MinExFilterExpression extends BaseFilterExpression {
    private MinExFilterExpression(Parameter parameter, Vocabulary vocabulary) {
        super(parameter, vocabulary, " > ");
    }

    public static MinExFilterExpression from(final Parameter.MinEx minEx, final Vocabulary vocabulary) {
        return new MinExFilterExpression(minEx, vocabulary);
    }

}
