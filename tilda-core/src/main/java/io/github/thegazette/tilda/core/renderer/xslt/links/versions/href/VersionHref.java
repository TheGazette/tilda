package io.github.thegazette.tilda.core.renderer.xslt.links.versions.href;

import io.github.thegazette.tilda.core.processor.context.input.InputContext;
import io.github.thegazette.tilda.core.api.viewer.Viewer;

import java.util.function.BiFunction;

public interface VersionHref extends BiFunction<InputContext, Viewer, String> {
}
