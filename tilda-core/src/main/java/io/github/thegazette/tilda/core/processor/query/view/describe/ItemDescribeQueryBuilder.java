package io.github.thegazette.tilda.core.processor.query.view.describe;

import io.github.thegazette.tilda.core.processor.query.view.construct.chains.PropertyChains;
import io.github.thegazette.tilda.core.api.viewer.LabelledDescribeViewer;
import io.github.thegazette.tilda.core.config.ConfigurationContext;
import io.github.thegazette.tilda.core.processor.query.view.ItemViewQueryBuilder;
import io.github.thegazette.tilda.core.processor.query.view.construct.generator.ItemGenerator;

import static org.eclipse.rdf4j.model.util.Values.iri;

public interface ItemDescribeQueryBuilder extends ItemViewQueryBuilder {
    interface Factory {
        static ItemDescribeQueryBuilder build(final ConfigurationContext config) {
            final var vocabulary = config.configuration().vocabulary();
            return (requestContext, item) -> {
                final var includeLabels = requestContext.viewer() instanceof LabelledDescribeViewer;

                var chains = PropertyChains.from("*");

                final var generator = new ItemGenerator(vocabulary);
                var generated = generator.generate(chains, item, "", "");

                return "CONSTRUCT " + generated.template() + "FROM <" + item + "> \r\n WHERE "+ generated.pattern();

            };
        }
    }

}
