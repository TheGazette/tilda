package io.github.thegazette.tilda.core.api.viewer;

import io.github.thegazette.tilda.core.util.Constants;

public record BasicViewer() implements Viewer {

    public static final String NAME = "basic";

    @Override
    public String iri() {
        return Constants.LDA.BASIC_VIEWER;
    }

    @Override
    public String name() {
        return NAME;
    }
}
