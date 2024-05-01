package uk.co.tso.tilda.web.handler;

import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import uk.co.tso.tilda.core.api.Selector;
import uk.co.tso.tilda.core.api.formatter.Formatter;
import uk.co.tso.tilda.core.api.variable.Variable;
import uk.co.tso.tilda.core.api.viewer.Viewer;
import uk.co.tso.tilda.core.config.EndpointConfigurationContext;
import uk.co.tso.tilda.core.processor.ItemsProcessor;
import uk.co.tso.tilda.core.processor.context.input.InputContext;
import uk.co.tso.tilda.core.processor.context.input.ItemsInputContext;
import uk.co.tso.tilda.web.handler.renderers.RendererResolver;
import uk.co.tso.tilda.core.config.ItemsEndpointConfigurationContext;
import uk.co.tso.tilda.web.handler.formatters.FormatterResolver;
import uk.co.tso.tilda.web.handler.selectors.SelectorResolver;
import uk.co.tso.tilda.web.handler.variables.EndpointVariableResolver;
import uk.co.tso.tilda.web.handler.viewers.ViewerResolver;

import java.util.List;
import java.util.Optional;

public interface ItemsEndpointHandlerFactory {
    static HandlerFunction<ServerResponse> build(final ItemsEndpointConfigurationContext config) {
        final var viewerResolver = ViewerResolver.Factory.build(config);
        final var variableResolver = EndpointVariableResolver.Factory.build(config);
        final var rendererResolver = RendererResolver.Factory.build(config);
        final var formatterResolver = FormatterResolver.Factory.build(config);
        final var selectorResolver = SelectorResolver.Factory.build(config);


        final var processor = ItemsProcessor.Factory.build(config);


        return (request) -> {
            final var vars = variableResolver.apply(request);
            final var viewer = viewerResolver.apply(request).orElseThrow();
            final var formatter = formatterResolver.apply(request);
            final var renderer = rendererResolver.apply(formatter).orElseThrow();
            final var selector = selectorResolver.apply(request);

            final var req = new ItemsInputContext() {

                @Override
                public EndpointConfigurationContext<?> configuration() {
                    return config;
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

                public Optional<Selector> selector() {
                    return selector;
                }
            };


            var res = processor.apply(req);

            return renderer.apply(req, res);
        };
    }

}
