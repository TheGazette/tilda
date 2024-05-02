package io.github.thegazette.tilda.core.processor.query.binding;

import io.github.thegazette.tilda.core.processor.context.input.InputContext;

import java.util.function.BiFunction;


public interface VariableBinder<R> extends BiFunction<InputContext, String, R> {
}
