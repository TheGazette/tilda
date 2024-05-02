package io.github.thegazette.tilda.core.processor.query.select.constructed.filtering.constructed.expressions;

import io.github.thegazette.tilda.core.processor.query.select.constructed.filtering.constructed.expressions.filter.MaxExFilterExpression;
import io.github.thegazette.tilda.core.processor.query.select.constructed.filtering.constructed.expressions.filter.MaxFilterExpression;
import io.github.thegazette.tilda.core.processor.query.select.constructed.filtering.constructed.expressions.filter.MinFilterExpression;
import io.github.thegazette.tilda.core.api.vocabulary.Vocabulary;
import io.github.thegazette.tilda.core.api.vocabulary.property.Property;
import io.github.thegazette.tilda.core.api.vocabulary.property.RdfTypeProperty;
import io.github.thegazette.tilda.core.processor.query.select.constructed.SelectQueryGenerator;
import io.github.thegazette.tilda.core.processor.query.select.constructed.filtering.constructed.expressions.filter.MinExFilterExpression;
import io.github.thegazette.tilda.core.processor.query.select.constructed.filtering.constructed.parameter.Parameter;

public interface FilterExpression {

    static FilterExpression from(final Parameter parameter, final Vocabulary vocabulary) {
        if (parameter instanceof Parameter.Plain plain)
            return PathExpression.from(plain, vocabulary);

        if (parameter instanceof Parameter.Min min)
            return MinFilterExpression.from(min, vocabulary);

        if (parameter instanceof Parameter.Max max)
            return MaxFilterExpression.from(max, vocabulary);

        if (parameter instanceof Parameter.MinEx minEx)
            return MinExFilterExpression.from(minEx, vocabulary);

        if (parameter instanceof Parameter.MaxEx maxEx)
            return MaxExFilterExpression.from(maxEx, vocabulary);

        if (parameter instanceof Parameter.Name name)
            return NameExpression.from(name, vocabulary);

        if (parameter instanceof Parameter.Exists exists)
            return ExistsExpression.from(exists, vocabulary);

        throw new UnsupportedOperationException("Unimplemented parameter type:" + parameter.getClass().descriptorString());
    }

    interface WithSameSubject extends FilterExpression {
        /**
         * The part that would go in the ?item ex:something "value" part of the query
         */
        SelectQueryGenerator.QueryBuilder.SameSubject sameSubject();
    }

    interface WithFilter extends FilterExpression {
        /**
         * the filter clauses that would come after the forQuery part
         */
        SelectQueryGenerator.QueryBuilder.Filter filter();
    }

    interface WithPattern extends FilterExpression {
        SelectQueryGenerator.QueryBuilder.Pattern pattern();
    }

    String name();

    static String queryableValue(Vocabulary vocabulary, Parameter parameter, Property property) {
        if (property instanceof RdfTypeProperty typeProperty) {
            var prop = vocabulary.getPropertyIRIByLabel(parameter.value());
            return (prop.startsWith("<") ? "" : "<") + prop + (prop.endsWith(">") ? "" : ">");
        }

        if (property.range().isPresent()) {
            var range = property.range().get();
            return "\"" + parameter.value() + "\"^^" + (range.startsWith("<") ? "" : "<") + range + (range.endsWith(">") ? "" : ">");
        }

        var prop = vocabulary
                .findPropertyIRIByLabel(parameter.value());

        return prop.orElseGet(() -> "\"" + parameter.value() + "\"");

    }
}
