package io.github.thegazette.tilda.core.renderer.xslt.links.formats;

import io.github.thegazette.tilda.core.processor.context.input.InputContext;
import io.github.thegazette.tilda.core.renderer.xslt.links.formats.href.ParameterBasedFormatHref;
import io.github.thegazette.tilda.core.renderer.xslt.links.formats.href.SuffixBasedFormatHref;
import org.slf4j.LoggerFactory;
import io.github.thegazette.tilda.core.api.formatter.Formatter;
import io.github.thegazette.tilda.core.config.EndpointConfigurationContext;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface AvailableFormats extends Function<InputContext, List<AvailableFormat>> {
    interface Factory {
        static AvailableFormats build(EndpointConfigurationContext<?> config) {
            final var logger = LoggerFactory.getLogger(AvailableFormats.class);

            final var formatters = config.configuration().formatters();

            final BiFunction<InputContext, Formatter, String> hreffer = switch (config.api().contentNegotiation()) {
                case SUFFIX -> SuffixBasedFormatHref.SUFFIX_BASED_FORMAT_HREF;
                case PARAMETER -> ParameterBasedFormatHref.PARAMETER_BASED_FORMAT_HREF;
            };

            return (inputContext) -> {
                final Function<Formatter, String> href = (format) -> hreffer.apply(inputContext, format);

                return formatters.stream()
                        .map(formatter -> new AvailableFormat(href.apply(formatter), formatter.name(), formatter.name()))
                        .toList();
            };

        }

    }
}
