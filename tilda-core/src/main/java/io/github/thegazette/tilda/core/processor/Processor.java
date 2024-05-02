package io.github.thegazette.tilda.core.processor;

import io.github.thegazette.tilda.core.processor.context.OutputContext;
import io.github.thegazette.tilda.core.processor.context.input.InputContext;

import java.util.function.Function;

public interface Processor<I extends InputContext> extends Function<I, OutputContext> {
}
