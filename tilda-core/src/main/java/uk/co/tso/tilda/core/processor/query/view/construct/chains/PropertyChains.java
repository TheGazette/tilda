package uk.co.tso.tilda.core.processor.query.view.construct.chains;

import com.google.common.collect.Maps;

import java.util.*;

public final class PropertyChains extends BasePropertyChain {
    private PropertyChains() {
    }

    public static PropertyChains instance() {
        return new PropertyChains();
    }
    public static PropertyChains from(String propertyChains) {
        final var chains = new PropertyChains();
        chains.addChains(propertyChains);
        return chains;
    }

    public void addChains(String propertyChains) {
        if (propertyChains == null || propertyChains.isEmpty())
            return;

        for (var chain : propertyChains.split(",")) {
            children(chain);
        }

    }


}
