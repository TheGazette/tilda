package io.github.thegazette.tilda.core.processor.query.select.constructed.filtering.constructed;

import io.github.thegazette.tilda.core.processor.query.select.constructed.filtering.constructed.expressions.FilterExpression;
import io.github.thegazette.tilda.core.processor.query.select.constructed.SelectQueryGenerator;

import java.util.List;

public abstract class BaseParameterFilter {
    protected List<SelectQueryGenerator.QueryBuilder.SameSubject> sameSubjects(List<FilterExpression> filterExpressions) {
        return filterExpressions.stream()
                .filter(f -> f instanceof FilterExpression.WithSameSubject)
                .map(f -> (FilterExpression.WithSameSubject) f)
                .map(FilterExpression.WithSameSubject::sameSubject)
                .toList();

    }

    protected List<SelectQueryGenerator.QueryBuilder.Filter> filters(List<FilterExpression> expressions) {
        return expressions.stream()
                .filter(f -> f instanceof FilterExpression.WithFilter)
                .map(f -> (FilterExpression.WithFilter) f)
                .map(FilterExpression.WithFilter::filter)
                .toList();
    }

    protected List<SelectQueryGenerator.QueryBuilder.Pattern> patterns(List<FilterExpression> expressions) {
        return expressions.stream()
                .filter(f -> f instanceof FilterExpression.WithPattern)
                .map(f -> (FilterExpression.WithPattern) f)
                .map(FilterExpression.WithPattern::pattern)
                .toList();

    }
}
