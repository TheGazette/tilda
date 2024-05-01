package uk.co.tso.tilda.core.renderer.xslt.links.versions.href;

import uk.co.tso.tilda.core.api.viewer.Viewer;
import uk.co.tso.tilda.core.processor.context.input.InputContext;

import java.util.function.BiFunction;

public interface VersionHref extends BiFunction<InputContext, Viewer, String> {
}
