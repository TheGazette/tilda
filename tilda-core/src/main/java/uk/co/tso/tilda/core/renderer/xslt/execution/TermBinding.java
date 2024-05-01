package uk.co.tso.tilda.core.renderer.xslt.execution;

import com.google.common.collect.ImmutableMap;
import org.eclipse.rdf4j.model.Resource;
import org.slf4j.LoggerFactory;
import uk.co.tso.tilda.core.config.EndpointConfigurationContext;
import uk.co.tso.tilda.core.processor.context.OutputContext;
import uk.co.tso.tilda.core.processor.context.input.InputContext;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

public interface TermBinding extends BiFunction<InputContext, OutputContext, Map<String, Object>> {
    interface Factory {
        static TermBinding build(EndpointConfigurationContext<?> config) {
            final var vocabulary = config.configuration().vocabulary();
            return (inputContext, outputContext) -> {
                var items = outputContext.model()
                        .predicates()
                        .stream()
                        .map(Resource::stringValue)
                        .map(vocabulary::findPropertyByValue)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .map(property -> ImmutableMap.<String, Object>builder()
                                .put("id", "_:term_" + property.label())
                                .put("property", property.iri().replace("<", "").replace(">", ""))
                                .put("label", property.label())
                                .build())
                        .toList();

                return ImmutableMap.<String, Object>builder()
                        .put("termBinding", items)
                        .build();
            };
        }

    }
}
