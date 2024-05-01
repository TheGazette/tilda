package uk.co.tso.tilda.core.api.viewer;

import uk.co.tso.tilda.core.util.Constants;

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
