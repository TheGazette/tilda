package io.github.thegazette.tilda.core.processor;

import io.github.thegazette.tilda.core.processor.context.OutputContext;
import io.github.thegazette.tilda.core.processor.context.input.InputContext;
import io.github.thegazette.tilda.core.processor.context.input.ItemInputContext;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.eclipse.rdf4j.repository.util.Repositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriComponentsBuilder;
import io.github.thegazette.tilda.core.processor.metadata.ItemMetadata;
import io.github.thegazette.tilda.core.processor.namespaces.Namespaces;
import io.github.thegazette.tilda.core.processor.query.view.ItemViewQueryBuilder;
import io.github.thegazette.tilda.core.config.ItemEndpointConfigurationContext;

import java.util.Map;
import java.util.function.Function;

public final class ItemProcessor implements Processor<ItemInputContext> {
    public interface Factory {
        static ItemProcessor build(ItemEndpointConfigurationContext config) {
            return new ItemProcessor(config);
        }
    }
    private final Logger logger = LoggerFactory.getLogger(ItemProcessor.class);

    private final String sparqlEndpoint;
    private final ItemViewQueryBuilder viewQueryBuilder;
    private final Function<InputContext, String> itemTemplate;
    private final Namespaces namespaces;
    private final SPARQLRepository repo;
    private final Function<String, Model> runQuery;
    private final ItemMetadata metadata;

    private ItemProcessor(ItemEndpointConfigurationContext config) {
        final var configuration = config.configuration();
        final var api = config.api();
        final var endpoint = config.endpoint();

        this.sparqlEndpoint = api.sparqlEndpoint();
        this.viewQueryBuilder = ItemViewQueryBuilder.Factory.build(config);
        this.namespaces = Namespaces.build(configuration, api, endpoint);
        this.repo = new SPARQLRepository(sparqlEndpoint);

        final var template = endpoint.itemTemplate();

        this.itemTemplate = (context) -> {
            final var itemTemplate = context.param("_template").orElse(template);
            final var builder = UriComponentsBuilder.fromUriString(itemTemplate);
            final Map<String, Object> vars = context.variableMap();
            return builder.uriVariables(vars).toUriString();
        };


        this.runQuery = (construct) ->
                Repositories.graphQueryNoTransaction(repo, construct, QueryResults::asModel);

        this.metadata = ItemMetadata.Factory.build(config);
    }

    @Override
    public OutputContext apply(ItemInputContext context) {
        var item = itemTemplate.apply(context);
        var construct =  viewQueryBuilder.apply(context, item);
        logger.debug("{}", construct);
        logger.debug("starting view query");
        var results = runQuery.apply(construct);
        logger.debug("ending view query");
        results.addAll(metadata.apply(context, item));
        namespaces.apply(results).forEach(results::setNamespace);
        return new OutputContext() {
            @Override
            public Model model() {
                return results;
            }
        };
    }
}
