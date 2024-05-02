package io.github.thegazette.tilda.core.renderer.xslt.links.formats.href;

import io.github.thegazette.tilda.core.processor.context.input.InputContext;
import io.github.thegazette.tilda.core.api.formatter.Formatter;

import java.util.function.BiFunction;

public interface FormatHref extends BiFunction<InputContext, Formatter, String> {
}
