package uk.co.tso.tilda.core.processor.context.input;

import uk.co.tso.tilda.core.api.Selector;

import java.util.Optional;

public interface ItemsInputContext extends InputContext {
    default Optional<Selector> selector() {
        return Optional.empty();
    }
}
