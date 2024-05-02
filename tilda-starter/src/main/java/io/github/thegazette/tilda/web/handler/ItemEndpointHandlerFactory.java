package io.github.thegazette.tilda.web.handler;

import io.github.thegazette.tilda.web.handler.formatters.FormatterResolver;
import io.github.thegazette.tilda.web.handler.renderers.RendererResolver;
import io.github.thegazette.tilda.web.handler.variables.ItemEndpointVariableResolver;
import io.github.thegazette.tilda.web.handler.viewers.ViewerResolver;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import io.github.thegazette.tilda.core.api.Selector;
import io.github.thegazette.tilda.core.api.formatter.Formatter;
import io.github.thegazette.tilda.core.api.variable.Variable;
import io.github.thegazette.tilda.core.api.viewer.Viewer;
import io.github.thegazette.tilda.core.config.EndpointConfigurationContext;
import io.github.thegazette.tilda.core.processor.ItemProcessor;
import io.github.thegazette.tilda.core.processor.context.input.ItemInputContext;
import io.github.thegazette.tilda.core.config.ItemEndpointConfigurationContext;

import java.util.List;
import java.util.Optional;

import static org.eclipse.rdf4j.model.util.Values.iri;

public interface ItemEndpointHandlerFactory {
    static HandlerFunction<ServerResponse>  build(final ItemEndpointConfigurationContext config) {
        final var viewerResolver = ViewerResolver.Factory.build(config);
        final var variableResolver = ItemEndpointVariableResolver.Factory.build(config);
        final var formatterResolver = FormatterResolver.Factory.build(config);
        final var rendererResolver = RendererResolver.Factory.build(config);

        final var processor = ItemProcessor.Factory.build(config);

        return request -> {
            final var vars = variableResolver.apply(request);
            final var viewer = viewerResolver.apply(request).orElseThrow();
            final var formatter = formatterResolver.apply(request);
            final var renderer = rendererResolver.apply(formatter).orElseThrow();

            final var req = new ItemInputContext() {
                @Override
                public EndpointConfigurationContext<?> configuration() {
                    return config;
                }

                public Optional<Selector> selector() {
                    return Optional.empty();
                }

                @Override
                public ServerRequest request() {
                    return request;
                }

                @Override
                public List<Variable> variables() {
                    return vars;
                }

                @Override
                public Viewer viewer() {
                    return viewer;
                }

                @Override
                public Formatter formatter() {
                    return formatter;
                }
            };


            var res = processor.apply(req);

            return renderer.apply(req, res);
        };
    }
}
