package io.github.thegazette.tilda.core.processor.query.view;

import io.github.thegazette.tilda.core.processor.query.view.construct.ItemsConstructQueryBuilder;
import io.github.thegazette.tilda.core.processor.query.view.describe.ItemsDescribeQueryBuilder;
import io.github.thegazette.tilda.core.api.viewer.DescribeViewer;
import io.github.thegazette.tilda.core.api.viewer.LabelledDescribeViewer;
import io.github.thegazette.tilda.core.api.viewer.Viewer;
import io.github.thegazette.tilda.core.config.ItemsEndpointConfigurationContext;

import java.util.List;

public interface ItemsViewQueryBuilder extends ViewQueryBuilder<List<String>> {

    interface Factory {
        static ItemsViewQueryBuilder build(ItemsEndpointConfigurationContext config) {
            final var constructQueryBuilder = ItemsConstructQueryBuilder.Factory.build(config);
            final var describeQueryBuilder = ItemsDescribeQueryBuilder.Factory.build(config);
            return (context, item) -> {
                final Viewer viewer = context.viewer();
                if (viewer instanceof DescribeViewer || viewer instanceof LabelledDescribeViewer)
                    return describeQueryBuilder.apply(context, item);

                return constructQueryBuilder.apply(context, item);
            };
        }
    }
}
