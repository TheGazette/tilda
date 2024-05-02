package io.github.thegazette.tilda.core.api.common;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface HasViewers {
    default Optional<String> defaultViewer() {
        return Optional.empty();
    }
    default List<String> viewers() {
        return Collections.emptyList();
    };
}
