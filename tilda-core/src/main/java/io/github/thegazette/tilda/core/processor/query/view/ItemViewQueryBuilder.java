package io.github.thegazette.tilda.core.processor.query.view;

import io.github.thegazette.tilda.core.processor.query.view.construct.ItemConstructQueryBuilder;
import io.github.thegazette.tilda.core.processor.query.view.describe.ItemDescribeQueryBuilder;
import io.github.thegazette.tilda.core.api.viewer.DescribeViewer;
import io.github.thegazette.tilda.core.api.viewer.LabelledDescribeViewer;
import io.github.thegazette.tilda.core.api.viewer.Viewer;
import io.github.thegazette.tilda.core.config.ItemEndpointConfigurationContext;

public interface ItemViewQueryBuilder extends ViewQueryBuilder<String> {
    interface Factory {
        static ItemViewQueryBuilder build(final ItemEndpointConfigurationContext config) {
            final var constructQueryBuilder = ItemConstructQueryBuilder.Factory.build(config);
            final var describeQueryBuilder = ItemDescribeQueryBuilder.Factory.build(config);

            return (context, item) -> {
                final Viewer viewer = context.viewer();
                if (viewer instanceof DescribeViewer || viewer instanceof LabelledDescribeViewer)
                    return describeQueryBuilder.apply(context, item);

                return constructQueryBuilder.apply(context, item);
            };
        }
    }
}
