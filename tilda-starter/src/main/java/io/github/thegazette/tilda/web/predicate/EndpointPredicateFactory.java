package io.github.thegazette.tilda.web.predicate;

import org.springframework.web.servlet.function.RequestPredicate;
import io.github.thegazette.tilda.core.config.ConfigurationContext;

public interface EndpointPredicateFactory {
    static RequestPredicate build(ConfigurationContext config) {
        return request -> true;
    }
}
