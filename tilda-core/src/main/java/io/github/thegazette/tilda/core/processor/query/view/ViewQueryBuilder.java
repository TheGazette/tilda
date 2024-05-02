package io.github.thegazette.tilda.core.processor.query.view;

import io.github.thegazette.tilda.core.processor.context.input.InputContext;

import java.util.function.BiFunction;

public interface ViewQueryBuilder<I> extends BiFunction<InputContext, I, String> {

}
