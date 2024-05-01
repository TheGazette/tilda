package uk.co.tso.tilda.core.renderer;

import org.springframework.web.servlet.function.ServerResponse;
import uk.co.tso.tilda.core.api.formatter.Formatter;
import uk.co.tso.tilda.core.processor.context.input.InputContext;
import uk.co.tso.tilda.core.processor.context.OutputContext;

import java.util.function.BiFunction;

public interface Renderer extends BiFunction<InputContext, OutputContext, ServerResponse> {
    Formatter formatter();
    default String name() {
        return formatter().name();
    }
}
