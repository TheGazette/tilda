package uk.co.tso.tilda.core.processor.query.view;

import uk.co.tso.tilda.core.api.viewer.DescribeViewer;
import uk.co.tso.tilda.core.api.viewer.LabelledDescribeViewer;
import uk.co.tso.tilda.core.api.viewer.Viewer;
import uk.co.tso.tilda.core.config.ItemsEndpointConfigurationContext;
import uk.co.tso.tilda.core.processor.query.view.ViewQueryBuilder;
import uk.co.tso.tilda.core.processor.query.view.construct.ItemsConstructQueryBuilder;
import uk.co.tso.tilda.core.processor.query.view.describe.ItemsDescribeQueryBuilder;

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
