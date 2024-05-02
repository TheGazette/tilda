package io.github.thegazette.tilda.core.renderer.xslt.links.formats.href;

public interface ParameterBasedFormatHref extends FormatHref {
    //TODO: Actually implement this
    ParameterBasedFormatHref PARAMETER_BASED_FORMAT_HREF = (inputContext, format) -> {
        return inputContext.request().uri().toString();
    };
}
