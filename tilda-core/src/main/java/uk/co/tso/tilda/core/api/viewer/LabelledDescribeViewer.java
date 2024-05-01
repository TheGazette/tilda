package uk.co.tso.tilda.core.api.viewer;

import uk.co.tso.tilda.core.util.Constants;

public record LabelledDescribeViewer() implements Viewer {

    public static final String NAME = "all";

    @Override
    public String iri() {
        return Constants.LDA.LABELLED_DESCRIBE_VIEWER;
    }

    @Override
    public String name() {
        return NAME;
    }

}
