package io.github.thegazette.tilda.core.renderer.xslt.execution;

import com.google.common.collect.ImmutableMap;
import io.github.thegazette.tilda.core.processor.context.OutputContext;
import io.github.thegazette.tilda.core.processor.context.input.InputContext;
import io.github.thegazette.tilda.core.config.EndpointConfigurationContext;

import java.util.Map;
import java.util.function.BiFunction;

public interface ExecutionAsMap extends BiFunction<InputContext, OutputContext, Map<String, Object>> {
    interface Factory {
        static ExecutionAsMap build(EndpointConfigurationContext<?> config) {

            final var termBinding = TermBinding.Factory.build(config);

            return (inputContext, outputContext) -> {
                final var wasResultOf = ImmutableMap.<String, Object>builder()
                        .put("id", "_:execution")
                        .putAll(termBinding.apply(inputContext, outputContext))
                        .build();

                final var b = ImmutableMap.<String, Object>builder();
                b.put("wasResultOf", wasResultOf);



                return b.build();
            };
        }

    }
}
