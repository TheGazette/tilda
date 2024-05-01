package uk.co.tso.tilda.core.processor.query.view.describe;

import uk.co.tso.tilda.core.api.viewer.LabelledDescribeViewer;
import uk.co.tso.tilda.core.config.ItemsEndpointConfigurationContext;
import uk.co.tso.tilda.core.processor.query.view.ItemsViewQueryBuilder;
import uk.co.tso.tilda.core.processor.query.view.construct.chains.PropertyChains;
import uk.co.tso.tilda.core.processor.query.view.construct.generator.ItemGenerator;
import uk.co.tso.tilda.core.processor.query.view.construct.generator.ItemsGenerator;

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
