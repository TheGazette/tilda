package uk.co.tso.tilda.core.renderer.xslt.execution;

import com.google.common.collect.ImmutableMap;
import org.eclipse.rdf4j.model.Resource;
import uk.co.tso.tilda.core.config.EndpointConfigurationContext;
import uk.co.tso.tilda.core.processor.context.OutputContext;
import uk.co.tso.tilda.core.processor.context.input.InputContext;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

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
