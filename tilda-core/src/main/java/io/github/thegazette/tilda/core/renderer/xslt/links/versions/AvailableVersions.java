package io.github.thegazette.tilda.core.renderer.xslt.links.versions;

import com.google.common.collect.ImmutableList;
import io.github.thegazette.tilda.core.processor.context.input.InputContext;
import io.github.thegazette.tilda.core.renderer.xslt.links.versions.href.ParameterBasedVersionHref;
import io.github.thegazette.tilda.core.renderer.xslt.links.versions.href.SuffixBasedVersionHref;
import io.github.thegazette.tilda.core.renderer.xslt.links.versions.href.VersionHref;
import io.github.thegazette.tilda.core.api.viewer.Viewer;
import io.github.thegazette.tilda.core.config.EndpointConfigurationContext;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface AvailableVersions extends Function<InputContext, List<AvailableVersion>> {
    interface Factory {
        static AvailableVersions build(EndpointConfigurationContext<?> config) {

            final var configuredViewers = config.configuration().viewers();

            final var apiViewerNames = config.api().viewers();
            final var endpointViewerNames = config.endpoint().viewers();

            final var viewers = ImmutableList.<String>builder()
                    .addAll(apiViewerNames)
                    .addAll(endpointViewerNames)
                    .build()
                    .stream()
                    .distinct()
                    .map(iri -> configuredViewers.stream().filter(v -> iri.equals(v.iri())).findFirst())
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();

            final VersionHref hreffer = switch (config.api().contentNegotiation()) {
                case SUFFIX ->  SuffixBasedVersionHref.SUFFIX_BASED_VERSION_HREF;
                case PARAMETER ->  ParameterBasedVersionHref.PARAMETER_BASED_VERSION_HREF;
            };

            return (inputContext) -> {
                final Function<Viewer, String> href = (viewer) -> hreffer.apply(inputContext, viewer);

                return viewers.stream()
                        .map(viewer -> new AvailableVersion(href.apply(viewer), viewer.name(), viewer.name()))
                        .toList();

            };
        }
    }
}
