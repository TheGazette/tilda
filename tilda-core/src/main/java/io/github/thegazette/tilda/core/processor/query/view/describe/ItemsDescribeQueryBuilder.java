package io.github.thegazette.tilda.core.processor.query.view.describe;

import io.github.thegazette.tilda.core.processor.query.view.construct.chains.PropertyChains;
import io.github.thegazette.tilda.core.api.viewer.LabelledDescribeViewer;
import io.github.thegazette.tilda.core.config.ItemsEndpointConfigurationContext;
import io.github.thegazette.tilda.core.processor.query.view.ItemsViewQueryBuilder;
import io.github.thegazette.tilda.core.processor.query.view.construct.generator.ItemsGenerator;

public interface ItemsDescribeQueryBuilder extends ItemsViewQueryBuilder {

    interface Factory {
        static ItemsDescribeQueryBuilder build(ItemsEndpointConfigurationContext config) {
            final var vocabulary = config.configuration().vocabulary();
            return (requestContext, item) -> {
                final var includeLabels = requestContext.viewer() instanceof LabelledDescribeViewer;

                var chains = PropertyChains.from("*");

                final var generator = new ItemsGenerator(vocabulary);
                var generated = generator.generate(chains, item, "", "");

                return "CONSTRUCT " + generated.template() + "WHERE "+ generated.pattern();

            };
        }
    }

}
