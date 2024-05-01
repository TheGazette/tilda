package uk.co.tso.tilda.core.processor.metadata;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.DynamicModelFactory;
import org.eclipse.rdf4j.model.util.Statements;
import org.eclipse.rdf4j.model.util.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.tso.tilda.core.config.ItemEndpointConfigurationContext;
import uk.co.tso.tilda.core.processor.ItemProcessor;
import uk.co.tso.tilda.core.util.Constants;
import uk.co.tso.tilda.core.processor.context.input.InputContext;

import java.util.function.BiFunction;

public interface ItemMetadata extends BiFunction<InputContext, String, Model> {

    interface Factory {

        final Logger logger = LoggerFactory.getLogger(ItemMetadata.class);

        static ItemMetadata build(ItemEndpointConfigurationContext config) {
            final var viewers = config.configuration().viewers();
            final var formatters = config.configuration().formatters();

            return (context, item) -> {

                final var model = new DynamicModelFactory().createEmptyModel();
                final IRI itemIRI = Values.iri(item);
                final IRI uriIRI = Values.iri(context.request().uri().toString());

                logger.debug("uriIRI :: " + uriIRI);
                final var isPrimaryTopicOfStmt = Statements.statement(itemIRI, Constants.FOAF_IS_PRIMARY_TOPIC_OF, uriIRI, null);
                final var primaryTopicStmt = Statements.statement(uriIRI, Constants.FOAF_PRIMARY_TOPIC, itemIRI, null);

                model.add(isPrimaryTopicOfStmt);
                model.add(primaryTopicStmt);

                return model;
            };
        }
    }

}
