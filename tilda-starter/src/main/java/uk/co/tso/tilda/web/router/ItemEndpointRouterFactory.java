package uk.co.tso.tilda.web.router;

import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;
import uk.co.tso.tilda.core.api.ContentNegotiation;
import uk.co.tso.tilda.core.api.formatter.Formatter;
import uk.co.tso.tilda.core.config.ItemEndpointConfigurationContext;
import uk.co.tso.tilda.web.handler.ItemEndpointHandlerFactory;
import uk.co.tso.tilda.web.predicate.EndpointPredicateFactory;

public interface ItemEndpointRouterFactory {
    static RouterFunction<ServerResponse> build(ItemEndpointConfigurationContext config) {
        if (ContentNegotiation.PARAMETER.equals(config.api().contentNegotiation()))
            return RouterFunctions.route().GET(
                            config.endpoint().uriTemplate(),
                            EndpointPredicateFactory.build(config),
                            ItemEndpointHandlerFactory.build(config))
                    .build();


        var ends = config.configuration().formatters().stream()
                .map(Formatter::name)
                .map(name -> {
                    return RouterFunctions.route().GET(
                                    config.endpoint().uriTemplate() + "." + name,
                                    EndpointPredicateFactory.build(config),
                                    ItemEndpointHandlerFactory.build(config))
                            .build();
                }).toList();
        var drf = RouterFunctions.route().GET(
                        config.endpoint().uriTemplate(),
                        EndpointPredicateFactory.build(config),
                        ItemEndpointHandlerFactory.build(config))
                .build();

        return ends.stream().reduce(RouterFunction::and).map(r -> r.and(drf)).orElse(drf);
    }

}
