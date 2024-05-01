package uk.co.tso.tilda.core.processor.query.binding;

import uk.co.tso.tilda.core.processor.context.input.InputContext;

import java.util.function.BiFunction;


public interface VariableBinder<R> extends BiFunction<InputContext, String, R> {
}
