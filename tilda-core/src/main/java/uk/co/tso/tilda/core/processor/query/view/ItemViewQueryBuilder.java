package uk.co.tso.tilda.core.processor.query.view;

import uk.co.tso.tilda.core.api.viewer.DescribeViewer;
import uk.co.tso.tilda.core.api.viewer.LabelledDescribeViewer;
import uk.co.tso.tilda.core.api.viewer.Viewer;
import uk.co.tso.tilda.core.config.EndpointConfigurationContext;
import uk.co.tso.tilda.core.config.ItemEndpointConfigurationContext;
import uk.co.tso.tilda.core.processor.query.view.construct.ItemConstructQueryBuilder;
import uk.co.tso.tilda.core.processor.query.view.describe.ItemDescribeQueryBuilder;

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
