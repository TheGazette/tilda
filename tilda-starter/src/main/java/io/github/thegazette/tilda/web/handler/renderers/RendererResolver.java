package io.github.thegazette.tilda.web.handler.renderers;

import io.github.thegazette.tilda.core.api.formatter.*;
import io.github.thegazette.tilda.core.renderer.*;
import io.github.thegazette.tilda.core.config.EndpointConfigurationContext;
import io.github.thegazette.tilda.core.renderer.xslt.XSLTRenderer;
import io.github.thegazette.tilda.core.renderer.simple.json.SimpleJSONRenderer;
import io.github.thegazette.tilda.core.renderer.simple.xml.SimpleXMLRenderer;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface RendererResolver extends Function<Formatter, Optional<Renderer>> {
    interface Factory {
        static RendererResolver build(EndpointConfigurationContext config) {
            final var configuration = config.configuration();

            final var renders = configuration.formatters().stream().map(formatter -> {
                if (formatter instanceof JsonLD)
                    return new JsonLDRenderer(formatter);
                if (formatter instanceof RdfJson)
                    return new RdfJsonRenderer(formatter);
                if (formatter instanceof RdfXml)
                    return new RdfXmlRenderer(formatter);
                if(formatter instanceof Turtle)
                    return new TurtleRenderer(formatter);
                if (formatter instanceof Json)
                    return SimpleJSONRenderer.from(formatter, configuration.vocabulary());
                if (formatter instanceof Xml)
                    return SimpleXMLRenderer.from(formatter, configuration.vocabulary());
                if (formatter instanceof Xslt xslt)
                    return XSLTRenderer.from(xslt, config);

                throw new RuntimeException();
            }).collect(Collectors.toMap(Renderer::name, Function.identity()));

            return (formatter) -> Optional.ofNullable(renders.get(formatter.name()));
        }
    }
}
