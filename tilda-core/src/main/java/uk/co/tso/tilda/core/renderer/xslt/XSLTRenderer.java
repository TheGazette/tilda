package uk.co.tso.tilda.core.renderer.xslt;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.io.input.ReaderInputStream;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.util.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.function.ServerResponse;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import uk.co.tso.tilda.core.api.formatter.Formatter;
import uk.co.tso.tilda.core.api.formatter.Xslt;
import uk.co.tso.tilda.core.api.vocabulary.Vocabulary;
import uk.co.tso.tilda.core.config.EndpointConfigurationContext;
import uk.co.tso.tilda.core.processor.context.OutputContext;
import uk.co.tso.tilda.core.processor.context.input.InputContext;
import uk.co.tso.tilda.core.renderer.Renderer;
import uk.co.tso.tilda.core.renderer.simple.intermediate.ModelToMap;
import uk.co.tso.tilda.core.renderer.simple.xml.SimpleXMLWriter;
import uk.co.tso.tilda.core.renderer.xslt.execution.ExecutionAsMap;
import uk.co.tso.tilda.core.renderer.xslt.links.formats.AvailableFormatsAsMap;
import uk.co.tso.tilda.core.renderer.xslt.links.versions.AvailableVersionsAsMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * This class is very much Gazettes specific at the moment, as Gazettes does not appear to
 * follow the spec in the following areas
 *
 * - It doesn't add the format/view metadata that the spec says should be added to the model before formatting
 *   (though there is code in Puelia to do just that). Instead, there is an "extendedMetadataVersion" that
 *   adds the available formats/views in a way that is similar, but not identical, to the spec.
 *   I have a feeling it's done this way because of the following
 *
 * - public facing URLs don't always match the defined template. In particular, while the notice template
 *   is /notice/{noticeid} the public url for the TTL /notice/{noticeid}/data.ttl?view=linked-data
 *
 */
public final class XSLTRenderer implements Renderer {

    private final Logger logger = LoggerFactory.getLogger(XSLTRenderer.class);
    private final Xslt formatter;
    private final byte[] stylesheet;
    private final URIResolver resolver;
    private final Vocabulary vocabulary;
    private final AvailableFormatsAsMap availableFormatsAsMap;
    private final AvailableVersionsAsMap availableVersionsAsMap;
    private final ExecutionAsMap executionAsMap;

    private XSLTRenderer(Xslt formatter, Vocabulary vocabulary, AvailableFormatsAsMap availableFormatsAsMap, AvailableVersionsAsMap availableVersionsAsMap, ExecutionAsMap executionAsMap) {
        this.formatter = formatter;
        this.availableFormatsAsMap = availableFormatsAsMap;
        this.availableVersionsAsMap = availableVersionsAsMap;
        this.executionAsMap = executionAsMap;
        final var loader = StylesheetLoader.from(formatter.prefix());
        this.stylesheet = loader.apply(formatter.stylesheet());
        this.resolver = (href, base) -> streamSource(loader.apply(href));
        this.vocabulary = vocabulary;
    }

    public static XSLTRenderer from(Xslt formatter, EndpointConfigurationContext<?> config) {
        final var configuration = config.configuration();
        final var vocabulary = configuration.vocabulary();

        final var availableFormatsAsMap = AvailableFormatsAsMap.Factory.build(config);
        final var availableVersionsAsMap = AvailableVersionsAsMap.Factory.build(config);
        final var executionAsMap = ExecutionAsMap.Factory.build(config);
        return new XSLTRenderer(formatter, vocabulary, availableFormatsAsMap, availableVersionsAsMap, executionAsMap);
    }


    @Override
    public ServerResponse apply(InputContext inputContext, OutputContext outputContext) {
        var model = outputContext.model();
        final IRI uriIRI = Values.iri(inputContext.request().uri().toString());

        final var modelAsMap = ModelToMap.with(model, vocabulary).apply(uriIRI);

        final var results = ImmutableMap.<String, Object>builder()
                .put("format", "linked-data-api")
                .put("version", "0.2")
                .put("results", ImmutableMap.<String, Object>builder()
                        .putAll(modelAsMap)
                        .putAll(availableFormatsAsMap.apply(inputContext))
                        .putAll(availableVersionsAsMap.apply(inputContext))
                        .putAll(executionAsMap.apply(inputContext, outputContext))
                        .build())
                .build();
        var sw = new StringWriter();

        var xmlWriter = new SimpleXMLWriter();
        xmlWriter.write(results, sw);

        var xml = "<?xml version=\"1.0\"?>\r\n" + sw.toString();
        logger.debug("xml:{}", xml);

        final var document = parseSource(xml);

        logger.debug("document:{}", document);

        final var out = new StringWriter();
        try {
            var source = streamSource(xml);
            transformer().transform(source, new StreamResult(out));
            out.flush();
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }

        var html = out.toString();
        logger.debug("html:{}", html);
        return ServerResponse.ok()
                .contentType(formatter.mediaType())
                .body(html);

    }

    @Override
    public Formatter formatter() {
        return formatter;
    }

    private Transformer transformer() {
        final var stylesource = streamSource(stylesheet);
        var t = TransformerFactory.newInstance();
        t.setURIResolver(resolver);

        try {
            return t.newTransformer(stylesource);
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
    private Document parseSource(final String source) {
        try {
            return DocumentBuilderFactory
                    .newDefaultInstance()
                    .newDocumentBuilder()
                    .parse(new ReaderInputStream(new StringReader(source), StandardCharsets.UTF_8));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static StreamSource streamSource(byte[] bytes) {
        return new StreamSource(new ByteArrayInputStream(bytes));
    }

    private static StreamSource streamSource(String s) {
        return new StreamSource(new ByteArrayInputStream(s.getBytes()));
    }
}
