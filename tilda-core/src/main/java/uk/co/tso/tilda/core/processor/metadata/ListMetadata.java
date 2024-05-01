package uk.co.tso.tilda.core.processor.metadata;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.DynamicModelFactory;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.util.RDFCollections;
import org.eclipse.rdf4j.model.util.Values;
import uk.co.tso.tilda.core.config.ItemsEndpointConfigurationContext;
import uk.co.tso.tilda.core.processor.query.select.SelectQuery;
import uk.co.tso.tilda.core.processor.context.input.InputContext;
import uk.co.tso.tilda.core.util.Constants;

import java.util.function.Function;

import static org.eclipse.rdf4j.model.util.Statements.statement;

public interface ListMetadata extends Function<ListMetadata.Context, Model> {
    record Context(InputContext inputContext, SelectQuery.SelectQueryContext selectQueryContext, Model results) {
        public static Context of(InputContext inputContext, SelectQuery.SelectQueryContext selectQueryContext, Model results) {
            return new Context(inputContext, selectQueryContext, results);
        }
    }

    interface Factory {
        static ListMetadata build(ItemsEndpointConfigurationContext config) {
            final var viewers = config.configuration().viewers();
            final var formatters = config.configuration().formatters();

            return (context) -> {
                final var processingContext = context.inputContext();
                final var queryContext = context.selectQueryContext();
                final var results = context.results();

                final var uri = processingContext.request().uri();
                final IRI iri = Values.iri(uri.toString());

                var typeStmt = statement(iri, Constants.RDF_TYPE, Constants.API_PAGE, null);

                var startIndexStmt = statement(iri, Constants.OPENSEARCH_START_INDEX,
                        Values.literal(queryContext.startIndex()), null);

                var itemsPerPageStmt = statement(iri, Constants.OPENSEARCH_ITEMS_PER_PAGE,
                        Values.literal(queryContext.itemsPerPage()), null);



/*
        var head = Values.bnode();
        final var items = RDFCollections.asRDF(results.subjects(), head, model);
        model.add(iri, Constants.API_ITEMS, head);
*/

                var head = Values.bnode("itemsList");
                final var items = RDFCollections.asRDF(results.subjects(), head, new LinkedHashModel());
                items.add(iri, Constants.API_ITEMS, head);

                final var model = new DynamicModelFactory().createEmptyModel();

                model.add(typeStmt);
                model.add(startIndexStmt);
                model.add(itemsPerPageStmt);
                model.add(iri, Constants.API_ITEMS, head);
                model.addAll(items);
                model.addAll(results);
                return model;
            };
        }
    }

}
