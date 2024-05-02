package io.github.thegazette.tilda.web.handler.selectors;

import org.springframework.web.servlet.function.ServerRequest;
import io.github.thegazette.tilda.core.api.Selector;
import io.github.thegazette.tilda.core.config.ItemsEndpointConfigurationContext;

import java.util.Optional;
import java.util.function.Function;

public interface SelectorResolver extends Function<ServerRequest, Optional<Selector>> {
    interface Factory {
        static SelectorResolver build(ItemsEndpointConfigurationContext config) {
            final var configuration = config.configuration();
            final var endpointSelector = config.endpoint().selector();

            if (endpointSelector != null && !endpointSelector.isEmpty()) {
                final var selector = configuration.selector(endpointSelector);
                return (request) -> selector;
            }

            return (request) -> Optional.empty();
        }
    }

}
