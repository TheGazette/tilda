package uk.co.tso.tilda.core.renderer.xslt.links.formats.href;

import uk.co.tso.tilda.core.api.formatter.Formatter;
import uk.co.tso.tilda.core.processor.context.input.InputContext;

import java.util.function.BiFunction;

public interface FormatHref extends BiFunction<InputContext, Formatter, String> {
}
