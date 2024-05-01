package uk.co.tso.tilda.core.processor;

import uk.co.tso.tilda.core.processor.context.OutputContext;
import uk.co.tso.tilda.core.processor.context.input.InputContext;

import java.util.function.Function;

public interface Processor<I extends InputContext> extends Function<I, OutputContext> {
}
