package io.github.thegazette.tilda.core.processor.query.view.construct;

import io.github.thegazette.tilda.core.processor.query.view.construct.chains.PropertyChains;
import io.github.thegazette.tilda.core.config.ConfigurationContext;
import io.github.thegazette.tilda.core.processor.query.view.ItemsViewQueryBuilder;
import io.github.thegazette.tilda.core.processor.query.view.construct.generator.ItemsGenerator;

public interface ItemsConstructQueryBuilder extends ItemsViewQueryBuilder {
    interface Factory {
        static ItemsConstructQueryBuilder build(final ConfigurationContext config) {
            final var vocabulary = config.configuration().vocabulary();
            return (context, items) -> {

                final var viewer = context.viewer();

                final var chains = PropertyChains.instance();
                context.properties().ifPresent(chains::addChains);
                viewer.propertyChain().ifPresent(chains::addChains);
                viewer.property().forEach(chains::addChains);

                final var generator = new ItemsGenerator(vocabulary);
                var generated = generator.generate(chains, items);

                return "CONSTRUCT " + generated.template() + " WHERE "+ generated.pattern();

            };
        }
    }

}
