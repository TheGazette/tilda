package io.github.thegazette.tilda.core.processor.context.input;

import io.github.thegazette.tilda.core.api.Selector;

import java.util.Optional;

public interface ItemsInputContext extends InputContext {
    default Optional<Selector> selector() {
        return Optional.empty();
    }
}
