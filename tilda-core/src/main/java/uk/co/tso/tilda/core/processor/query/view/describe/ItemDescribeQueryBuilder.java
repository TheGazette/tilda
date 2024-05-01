package uk.co.tso.tilda.core.processor.query.view.describe;

import uk.co.tso.tilda.core.api.viewer.LabelledDescribeViewer;
import uk.co.tso.tilda.core.config.ConfigurationContext;
import uk.co.tso.tilda.core.processor.query.view.ItemViewQueryBuilder;
import uk.co.tso.tilda.core.processor.query.view.construct.chains.PropertyChains;
import uk.co.tso.tilda.core.processor.query.view.construct.generator.Generator;
import uk.co.tso.tilda.core.processor.query.view.ViewQueryBuilder;
import uk.co.tso.tilda.core.processor.query.view.construct.generator.ItemGenerator;
import uk.co.tso.tilda.core.processor.query.view.construct.generator.ItemsGenerator;

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
