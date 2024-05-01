package uk.co.tso.tilda.core.renderer.xslt.links.formats;

import com.google.common.collect.ImmutableMap;
import org.slf4j.LoggerFactory;
import uk.co.tso.tilda.core.config.ConfigurationContext;
import uk.co.tso.tilda.core.config.EndpointConfigurationContext;
import uk.co.tso.tilda.core.processor.context.input.InputContext;

import java.util.Map;
import java.util.function.Function;

public interface AvailableFormatsAsMap extends Function<InputContext, Map<String, Object>> {

    interface Factory {
        static AvailableFormatsAsMap build(EndpointConfigurationContext<?> config) {
            final AvailableFormats availableFormats = AvailableFormats.Factory.build(config);
            final var logger = LoggerFactory.getLogger(AvailableFormatsAsMap.class);
            final var formatAsMap = FormatAsMap.Factory.build(config);
            return (inputContext) -> {
                var b = ImmutableMap.<String, Object>builder();

                var formats = availableFormats.apply(inputContext).stream().map(formatAsMap).toList();

                b.put("format", formats);

                logger.info(inputContext.request().path());

                return b.build();

            };
        }
    }



    interface FormatAsMap extends Function<AvailableFormat, Map<String, Object>> {
        interface Factory {
            static FormatAsMap build(ConfigurationContext config) {
                return (format) -> {
                    return ImmutableMap.<String, Object>builder()
                            .put("_about", format.href())
                            .put("label", format.name())
                            .put("name", format.name())
                            .build();
                };
            }
        }
    }
}
