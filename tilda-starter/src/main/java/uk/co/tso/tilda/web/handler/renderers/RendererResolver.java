package uk.co.tso.tilda.web.handler.renderers;

import uk.co.tso.tilda.core.api.formatter.*;
import uk.co.tso.tilda.core.config.APIConfigurationContext;
import uk.co.tso.tilda.core.config.EndpointConfigurationContext;
import uk.co.tso.tilda.core.renderer.*;
import uk.co.tso.tilda.core.renderer.xslt.XSLTRenderer;
import uk.co.tso.tilda.core.renderer.simple.json.SimpleJSONRenderer;
import uk.co.tso.tilda.core.renderer.simple.xml.SimpleXMLRenderer;
import uk.co.tso.tilda.core.config.ConfigurationContext;

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
