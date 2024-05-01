package uk.co.tso.tilda.core.renderer.xslt.links.formats.href;

import uk.co.tso.tilda.core.api.formatter.Formatter;
import uk.co.tso.tilda.core.processor.context.input.InputContext;

import java.util.function.BiFunction;

public interface ParameterBasedFormatHref extends FormatHref {
    //TODO: Actually implement this
    ParameterBasedFormatHref PARAMETER_BASED_FORMAT_HREF = (inputContext, format) -> {
        return inputContext.request().uri().toString();
    };
}
