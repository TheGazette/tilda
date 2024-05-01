package uk.co.tso.tilda.web.predicate;

import org.springframework.web.servlet.function.RequestPredicate;
import uk.co.tso.tilda.core.config.ConfigurationContext;

public interface EndpointPredicateFactory {
    static RequestPredicate build(ConfigurationContext config) {
        return request -> true;
    }
}
