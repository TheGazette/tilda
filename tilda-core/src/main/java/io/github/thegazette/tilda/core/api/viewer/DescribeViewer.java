package io.github.thegazette.tilda.core.api.viewer;

import io.github.thegazette.tilda.core.util.Constants;

public record DescribeViewer() implements Viewer {
    public static final String NAME = "description";
    @Override
    public String iri() {
        return Constants.LDA.DESCRIBE_VIEWER;
    }

    @Override
    public String name() {
        return NAME;
    }


}
