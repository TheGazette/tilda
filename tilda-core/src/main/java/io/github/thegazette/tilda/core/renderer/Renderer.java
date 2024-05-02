package io.github.thegazette.tilda.core.renderer;

import io.github.thegazette.tilda.core.processor.context.OutputContext;
import io.github.thegazette.tilda.core.processor.context.input.InputContext;
import org.springframework.web.servlet.function.ServerResponse;
import io.github.thegazette.tilda.core.api.formatter.Formatter;

import java.util.function.BiFunction;

public interface Renderer extends BiFunction<InputContext, OutputContext, ServerResponse> {
    Formatter formatter();
    default String name() {
        return formatter().name();
    }
}
