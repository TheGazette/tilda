package uk.co.tso.tilda.core.renderer.xslt.links.versions;

import com.google.common.collect.ImmutableMap;
import uk.co.tso.tilda.core.config.ConfigurationContext;
import uk.co.tso.tilda.core.config.EndpointConfigurationContext;
import uk.co.tso.tilda.core.processor.context.input.InputContext;

import java.util.Map;
import java.util.function.Function;

public interface AvailableVersionsAsMap extends Function<InputContext, Map<String, Object>> {
    interface Factory {
        static AvailableVersionsAsMap build(EndpointConfigurationContext<?> config) {
            final AvailableVersions availableVersions = AvailableVersions.Factory.build(config);
            final Function<AvailableVersion, Map<String, Object>> asMap = (version) -> ImmutableMap.<String, Object>builder()
                    .put("_about", version.href())
                    .put("label", version.name())
                    .put("name", version.name())
                    .build();
            return (inputContext) -> {
                var b = ImmutableMap.<String, Object>builder();
                var versions = availableVersions.apply(inputContext).stream().map(asMap).toList();
                b.put("version", versions);
                return b.build();
            };
        }
    }

}
