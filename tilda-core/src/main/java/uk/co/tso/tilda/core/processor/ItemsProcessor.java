package uk.co.tso.tilda.core.processor;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.eclipse.rdf4j.repository.util.Repositories;
import org.slf4j.LoggerFactory;
import uk.co.tso.tilda.core.processor.context.OutputContext;
import uk.co.tso.tilda.core.processor.context.input.ItemsInputContext;
import uk.co.tso.tilda.core.processor.metadata.ListMetadata;
import uk.co.tso.tilda.core.processor.namespaces.Namespaces;
import uk.co.tso.tilda.core.processor.query.select.SelectQuery;
import uk.co.tso.tilda.core.processor.query.view.ItemsViewQueryBuilder;
import uk.co.tso.tilda.core.processor.query.view.ViewQueryBuilder;
import uk.co.tso.tilda.core.config.ItemsEndpointConfigurationContext;

import java.util.List;
import java.util.function.Function;

public interface ItemsProcessor extends Processor<ItemsInputContext> {
    interface Factory {
        static ItemsProcessor build(ItemsEndpointConfigurationContext config) {
            final var configuration = config.configuration();
            final var api = config.api();
            final var endpoint = config.endpoint();

            final var sparqlEndpoint = api.sparqlEndpoint();
            final var selectQuery = SelectQuery.build(configuration, api, endpoint);
            final var logger = LoggerFactory.getLogger(ItemsProcessor.class);
            final var viewQueryBuilder = ItemsViewQueryBuilder.Factory.build(config);
            final var repo = new SPARQLRepository(sparqlEndpoint);
            final var namespaces = Namespaces.build(configuration, api, endpoint);

            final Function<TupleQueryResult, List<String>> asItems =
                    (r) -> r.stream().map(b -> b.getValue("item")).map(Value::stringValue).toList();

            final Function<String, List<String>> tupleQuery = (select) -> Repositories.tupleQueryNoTransaction(repo, select, asItems);

            final Function<String, Model> graphQuery =
                    (construct) -> Repositories.graphQueryNoTransaction(repo, construct, QueryResults::asModel);

            final var listMetadata = ListMetadata.Factory.build(config);

            return (context) -> {
                final var select = selectQuery.apply(context);
                var items = tupleQuery.apply(select.query());

                var construct = viewQueryBuilder.apply(context, items);
                logger.debug("{}", construct);
                var results = graphQuery.apply(construct);
                var metadata = listMetadata.apply(ListMetadata.Context.of(context, select, results));

                namespaces.apply(metadata).forEach(metadata::setNamespace);

                return new OutputContext() {
                    @Override
                    public Model model() {
                        return metadata;
                    }
                };
            };
        }
    }
}
