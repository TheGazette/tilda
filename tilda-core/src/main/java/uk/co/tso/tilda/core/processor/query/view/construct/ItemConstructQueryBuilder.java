package uk.co.tso.tilda.core.processor.query.view.construct;

import uk.co.tso.tilda.core.api.viewer.SpecialisedViewer;
import uk.co.tso.tilda.core.config.ConfigurationContext;
import uk.co.tso.tilda.core.processor.query.view.ItemViewQueryBuilder;
import uk.co.tso.tilda.core.processor.query.view.construct.chains.PropertyChains;
import uk.co.tso.tilda.core.processor.query.view.construct.generator.ItemGenerator;

public interface ItemConstructQueryBuilder extends ItemViewQueryBuilder {
    interface Factory {
        static ItemConstructQueryBuilder build(final ConfigurationContext config) {

            final var vocabulary = config.configuration().vocabulary();

            return (context, item) -> {
                final var viewer = context.viewer();

                final var chains = PropertyChains.instance();
                context.properties().ifPresent(chains::addChains);
                viewer.propertyChain().ifPresent(chains::addChains);
                viewer.property().forEach(chains::addChains);

                final var generator = new ItemGenerator(vocabulary);



                if (viewer instanceof SpecialisedViewer specialisedViewer) {
                    final var additionalTriples = specialisedViewer
                            .additionalTriples()
                            .map(s -> s.replace("{$item}", item))
                            .orElse("");

                    final var unionTemplate = specialisedViewer
                            .unionTemplate()
                            .map(s -> s.replace("{$item}", item))
                            .orElse("");

                    final var generated = generator.generate(chains, item, additionalTriples, unionTemplate);

                    final String construct;

                    if (specialisedViewer.includeNamespaces().equals(SpecialisedViewer.IncludeNamespaces.YES)) {
                        StringBuilder namespaces = new StringBuilder();
                        vocabulary.namespaces().forEach((key, value) -> {
                            namespaces.append("PREFIX " + key + ":");
                            namespaces.append("<" + value + ">\n");
                        });

                        construct = namespaces.append("CONSTRUCT ").toString();
                    } else {
                        construct = "CONSTRUCT ";
                    }

                    return switch (specialisedViewer.includeDatasetClause()) {
                        case DEFAULT, YES -> construct + generated.template() + "FROM <" + item + "> \r\n WHERE " + generated.pattern();
                        case NO -> construct + generated.template() + "\r\n WHERE " + generated.pattern();
                    };
                } else {
                    var generated = generator.generate(chains, item);
                    return "CONSTRUCT " + generated.template() + "FROM <" + item + "> \r\n WHERE " + generated.pattern();
                }

            };
        }
    }
}
