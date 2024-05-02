package io.github.thegazette.tilda.web.router;

import io.github.thegazette.tilda.web.predicate.EndpointPredicateFactory;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;
import io.github.thegazette.tilda.core.api.ContentNegotiation;
import io.github.thegazette.tilda.core.api.formatter.Formatter;
import io.github.thegazette.tilda.core.config.ItemEndpointConfigurationContext;
import io.github.thegazette.tilda.web.handler.ItemEndpointHandlerFactory;

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
