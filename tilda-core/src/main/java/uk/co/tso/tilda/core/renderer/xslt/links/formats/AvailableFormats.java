package uk.co.tso.tilda.core.renderer.xslt.links.formats;

import org.slf4j.LoggerFactory;
import uk.co.tso.tilda.core.api.formatter.Formatter;
import uk.co.tso.tilda.core.config.ConfigurationContext;
import uk.co.tso.tilda.core.config.EndpointConfigurationContext;
import uk.co.tso.tilda.core.processor.context.input.InputContext;
import uk.co.tso.tilda.core.renderer.xslt.links.formats.href.FormatHref;
import uk.co.tso.tilda.core.renderer.xslt.links.formats.href.ParameterBasedFormatHref;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static uk.co.tso.tilda.core.renderer.xslt.links.formats.href.ParameterBasedFormatHref.PARAMETER_BASED_FORMAT_HREF;
import static uk.co.tso.tilda.core.renderer.xslt.links.formats.href.SuffixBasedFormatHref.SUFFIX_BASED_FORMAT_HREF;

public interface AvailableFormats extends Function<InputContext, List<AvailableFormat>> {
    interface Factory {
        static AvailableFormats build(EndpointConfigurationContext<?> config) {
            final var logger = LoggerFactory.getLogger(AvailableFormats.class);

            final var formatters = config.configuration().formatters();

            final BiFunction<InputContext, Formatter, String> hreffer = switch (config.api().contentNegotiation()) {
                case SUFFIX -> SUFFIX_BASED_FORMAT_HREF;
                case PARAMETER -> PARAMETER_BASED_FORMAT_HREF;
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
